package com.example.hotelpricingadd.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.hotelpricingadd.config.AppProperties;
import com.example.hotelpricingadd.model.AgentTurn;
import com.example.hotelpricingadd.model.ArtifactLocations;
import com.example.hotelpricingadd.model.AssignmentRunResult;
import com.example.hotelpricingadd.model.InteractionCostSummary;
import com.example.hotelpricingadd.model.IterationResult;
import com.example.hotelpricingadd.model.RunDirectories;
import com.example.hotelpricingadd.model.RunRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Service
public class ArtifactService {

    private final AppProperties appProperties;
    private final ObjectMapper objectMapper;
    private final ReportComposer reportComposer;

    public ArtifactService(AppProperties appProperties, ObjectMapper objectMapper, ReportComposer reportComposer) {
        this.appProperties = appProperties;
        this.objectMapper = objectMapper.copy().findAndRegisterModules().enable(SerializationFeature.INDENT_OUTPUT);
        this.reportComposer = reportComposer;
    }

    public AssignmentRunResult publishRun(
            String runId,
            Instant startedAt,
            Instant finishedAt,
            InteractionCostSummary interactionCost,
            List<IterationResult> iterations,
            List<AgentTurn> turns,
            RunRequest request) {
        RunDirectories directories = prepareDirectories(runId);
        ArtifactLocations artifactLocations = new ArtifactLocations(
                directories.runDirectory().toString(),
                directories.reportFile().toString(),
                directories.summaryFile().toString(),
                directories.conversationsDirectory().toString(),
                directories.iterationsDirectory().toString());

        AssignmentRunResult result = new AssignmentRunResult(
                runId,
                appProperties.getAssignmentName(),
                appProperties.getParadigm(),
                appProperties.getLlmName(),
                startedAt,
                finishedAt,
                interactionCost,
                List.copyOf(iterations),
                artifactLocations);

        writeIterationSnapshots(directories.iterationsDirectory(), iterations);
        writeConversationLogs(directories.conversationsDirectory(), turns);
        writeTextFile(directories.reportFile(), reportComposer.compose(result, turns, request));
        writeJsonFile(directories.summaryFile(), result);
        return result;
    }

    private RunDirectories prepareDirectories(String runId) {
        Path runDirectory = appProperties.getArtifactRoot().resolve(runId).toAbsolutePath().normalize();
        Path conversationsDirectory = runDirectory.resolve("conversations");
        Path iterationsDirectory = runDirectory.resolve("iterations");
        Path reportFile = runDirectory.resolve("report.md");
        Path summaryFile = runDirectory.resolve("summary.json");

        try {
            Files.createDirectories(conversationsDirectory);
            Files.createDirectories(iterationsDirectory);
        }
        catch (IOException ex) {
            throw new IllegalStateException("Failed to create artifact directories for run " + runId, ex);
        }

        return new RunDirectories(runDirectory, reportFile, summaryFile, conversationsDirectory, iterationsDirectory);
    }

    private void writeIterationSnapshots(Path iterationsDirectory, List<IterationResult> iterations) {
        for (IterationResult iteration : iterations) {
            Path targetFile = iterationsDirectory.resolve("iteration-%02d.json".formatted(iteration.iterationNumber()));
            writeJsonFile(targetFile, iteration);
        }
    }

    private void writeConversationLogs(Path conversationsDirectory, List<AgentTurn> turns) {
        Map<Integer, List<AgentTurn>> grouped = turns.stream()
                .sorted(Comparator.comparing(AgentTurn::timestamp))
                .collect(Collectors.groupingBy(AgentTurn::iterationNumber));

        writeJsonFile(conversationsDirectory.resolve("all-turns.json"), turns);

        grouped.forEach((iterationNumber, iterationTurns) -> {
            List<AgentTurn> sortedTurns = iterationTurns.stream()
                    .sorted(Comparator.comparing(AgentTurn::timestamp))
                    .toList();
            StringBuilder builder = new StringBuilder();
            builder.append("# Iteration ").append(iterationNumber).append(" Conversation Log\n\n");
            builder.append("- Generated at: ").append(Instant.now()).append('\n');
            builder.append("- Total turns: ").append(sortedTurns.size()).append("\n\n");
            int turnIndex = 1;
            for (AgentTurn turn : sortedTurns) {
                builder.append("## Turn ").append(turnIndex++).append(" - ").append(turn.role().getDisplayName()).append("\n\n");
                builder.append("- Timestamp: ").append(turn.timestamp()).append('\n');
                builder.append("- Latency (ms): ").append(turn.latencyMillis()).append('\n');
                builder.append("- Prompt tokens: ").append(formatNullable(turn.usage().promptTokens())).append('\n');
                builder.append("- Completion tokens: ").append(formatNullable(turn.usage().completionTokens())).append('\n');
                builder.append("- Total tokens: ").append(formatNullable(turn.usage().totalTokens())).append("\n\n");
                builder.append("### System Prompt\n\n```\n").append(turn.systemPrompt()).append("\n```\n\n");
                builder.append("### User Prompt\n\n```\n").append(turn.userPrompt()).append("\n```\n\n");
                builder.append("### Model Response\n\n```\n").append(turn.response()).append("\n```\n\n");
            }
            writeTextFile(conversationsDirectory.resolve("iteration-%02d.md".formatted(iterationNumber)), builder.toString());
        });
    }

    private String formatNullable(Integer value) {
        return value == null ? "N/A" : value.toString();
    }

    private void writeJsonFile(Path path, Object payload) {
        try {
            Files.createDirectories(path.getParent());
            objectMapper.writeValue(path.toFile(), payload);
        }
        catch (IOException ex) {
            throw new IllegalStateException("Failed to write JSON artifact: " + path, ex);
        }
    }

    private void writeTextFile(Path path, String content) {
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, content, StandardCharsets.UTF_8);
        }
        catch (IOException ex) {
            throw new IllegalStateException("Failed to write text artifact: " + path, ex);
        }
    }
}
