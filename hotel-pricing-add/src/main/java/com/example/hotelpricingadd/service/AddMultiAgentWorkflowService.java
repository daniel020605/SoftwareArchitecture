package com.example.hotelpricingadd.service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.hotelpricingadd.config.AppProperties;
import com.example.hotelpricingadd.model.AgentTurn;
import com.example.hotelpricingadd.model.AssignmentRunResult;
import com.example.hotelpricingadd.model.InteractionCostSummary;
import com.example.hotelpricingadd.model.IterationDefinition;
import com.example.hotelpricingadd.model.IterationResult;
import com.example.hotelpricingadd.model.RunRequest;
import com.example.hotelpricingadd.util.JsonBlockExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AddMultiAgentWorkflowService {

    private static final DateTimeFormatter RUN_ID_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(ZoneOffset.UTC);

    private final AgentGateway agentGateway;
    private final PromptFactory promptFactory;
    private final ArtifactService artifactService;
    private final AppProperties appProperties;
    private final ObjectMapper objectMapper;

    public AddMultiAgentWorkflowService(
            AgentGateway agentGateway,
            PromptFactory promptFactory,
            ArtifactService artifactService,
            AppProperties appProperties,
            ObjectMapper objectMapper) {
        this.agentGateway = agentGateway;
        this.promptFactory = promptFactory;
        this.artifactService = artifactService;
        this.appProperties = appProperties;
        this.objectMapper = objectMapper;
    }

    public AssignmentRunResult run(RunRequest request) {
        Instant startedAt = Instant.now();
        String runId = "run-" + RUN_ID_FORMATTER.format(startedAt);

        List<IterationResult> iterationResults = new ArrayList<>();
        List<AgentTurn> turns = new ArrayList<>();
        int reviewerRounds = request.getReviewerRounds() == null
                ? appProperties.getReviewerRounds()
                : request.getReviewerRounds();

        for (IterationDefinition iteration : AssignmentCatalog.iterations()) {
            AgentTurn analyst = agentGateway.invoke(promptFactory.analystRequest(iteration, iterationResults, request));
            turns.add(analyst);

            AgentTurn architectVersion = agentGateway.invoke(
                    promptFactory.architectDraftRequest(iteration, iterationResults, analyst.response(), request));
            turns.add(architectVersion);

            AgentTurn latestReview = null;
            for (int round = 0; round < reviewerRounds; round++) {
                latestReview = agentGateway.invoke(
                        promptFactory.reviewerRequest(iteration, iterationResults, analyst.response(), architectVersion.response()));
                turns.add(latestReview);

                architectVersion = agentGateway.invoke(
                        promptFactory.architectRevisionRequest(iteration, iterationResults, architectVersion.response(), latestReview.response()));
                turns.add(architectVersion);
            }

            AgentTurn diagram = agentGateway.invoke(
                    promptFactory.diagramRequest(iteration, architectVersion.response(), request));
            turns.add(diagram);

            AgentTurn moderator = agentGateway.invoke(
                    promptFactory.moderatorRequest(
                            iteration,
                            analyst.response(),
                            architectVersion.response(),
                            latestReview == null ? "No review round executed." : latestReview.response(),
                            diagram.response(),
                            request));
            turns.add(moderator);

            IterationResult iterationResult = normalizeIterationResult(parseIterationResult(moderator.response()), iteration, request);
            iterationResults.add(iterationResult);
        }

        Instant finishedAt = Instant.now();
        InteractionCostSummary interactionCost = summarizeInteractionCost(turns, request, startedAt, finishedAt);
        return artifactService.publishRun(runId, startedAt, finishedAt, interactionCost, iterationResults, turns, request);
    }

    private IterationResult parseIterationResult(String response) {
        try {
            String json = JsonBlockExtractor.extract(response);
            return objectMapper.readValue(json, IterationResult.class);
        }
        catch (Exception ex) {
            throw new IllegalStateException("Failed to parse moderator JSON response.", ex);
        }
    }

    private IterationResult normalizeIterationResult(IterationResult result, IterationDefinition definition, RunRequest request) {
        return new IterationResult(
                definition.number(),
                fallback(result.iterationTitle(), definition.title()),
                fallback(result.iterationGoal(), definition.focus()),
                safeList(result.selectedDrivers(), definition.suggestedDrivers()),
                fallback(result.addStep2(), "Not provided by the model."),
                fallback(result.addStep3(), "Not provided by the model."),
                fallback(result.addStep4(), "Not provided by the model."),
                fallback(result.addStep5(), "Not provided by the model."),
                fallback(result.addStep6(), "Not provided by the model."),
                fallback(result.addStep7(), "Not provided by the model."),
                fallback(result.diagramType(), promptFactory.resolvedDiagramType(request)),
                fallback(result.diagramCode(), ""),
                safeList(result.keyDecisions(), List.of("No decisions were extracted.")),
                safeList(result.followUpRisks(), List.of("No follow-up risks were extracted.")));
    }

    private InteractionCostSummary summarizeInteractionCost(
            List<AgentTurn> turns,
            RunRequest request,
            Instant startedAt,
            Instant finishedAt) {
        Integer promptTokens = sumNullable(turns.stream().map(turn -> turn.usage().promptTokens()).toList());
        Integer completionTokens = sumNullable(turns.stream().map(turn -> turn.usage().completionTokens()).toList());
        Integer totalTokens = sumNullable(turns.stream().map(turn -> turn.usage().totalTokens()).toList());
        int humanTurns = request.getHumanInteractionTurns() == null ? 1 : request.getHumanInteractionTurns();
        long elapsedSeconds = Duration.between(startedAt, finishedAt).toSeconds();
        return new InteractionCostSummary(turns.size(), humanTurns, promptTokens, completionTokens, totalTokens, elapsedSeconds);
    }

    private Integer sumNullable(List<Integer> values) {
        Integer sum = null;
        for (Integer value : values) {
            if (value != null) {
                sum = sum == null ? value : sum + value;
            }
        }
        return sum;
    }

    private String fallback(String candidate, String fallback) {
        return candidate == null || candidate.isBlank() ? fallback : candidate;
    }

    private List<String> safeList(List<String> candidate, List<String> fallback) {
        if (candidate == null || candidate.isEmpty()) {
            return List.copyOf(fallback);
        }
        return candidate;
    }
}
