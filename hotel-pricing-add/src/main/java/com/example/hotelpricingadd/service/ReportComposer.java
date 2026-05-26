package com.example.hotelpricingadd.service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.hotelpricingadd.config.AppProperties;
import com.example.hotelpricingadd.model.AgentTurn;
import com.example.hotelpricingadd.model.AssignmentRunResult;
import com.example.hotelpricingadd.model.IterationResult;
import com.example.hotelpricingadd.model.RunRequest;

@Component
public class ReportComposer {

    private final AppProperties appProperties;

    public ReportComposer(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String compose(AssignmentRunResult result, List<AgentTurn> turns, RunRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append("# Software Architecture Assignment 2 Report\n\n");
        builder.append("## Project Metadata\n\n");
        builder.append("- Assignment selection: ").append(appProperties.getParadigm()).append('\n');
        builder.append("- LLM used: ").append(appProperties.getLlmName()).append('\n');
        builder.append("- Run ID: ").append(result.runId()).append('\n');
        builder.append("- Team name: ").append(safeValue(request.getTeamName())).append('\n');
        builder.append("- Team members: ").append(formatMembers(request.getTeamMembers())).append("\n\n");
        builder.append("## Submission Note\n\n");
        builder.append("This draft was generated from the multi-agent ADD workflow. Before submission, the team should review wording, verify that each architectural claim matches the latest run artifacts, and complete the individual reflection section with real group contributions.\n\n");

        builder.append("## 1. Output results of ADD\n\n");
        builder.append("### ADD Step 1\n\n");
        builder.append("The architectural drivers reviewed before the iterations were the six primary use cases (HPS-1 to HPS-6), the nine quality attribute scenarios (QA-1 to QA-9), the five architectural concerns (CRN-1 to CRN-5), and the six constraints (CON-1 to CON-6). The design activity treated the case as greenfield development and used the fixed four-iteration plan from the assignment.\n\n");

        for (IterationResult iteration : result.iterations()) {
            builder.append("### Iteration ").append(iteration.iterationNumber()).append(": ").append(iteration.iterationTitle()).append("\n\n");
            builder.append("- Iteration goal: ").append(iteration.iterationGoal()).append('\n');
            builder.append("- Selected drivers: ").append(joinList(iteration.selectedDrivers())).append("\n\n");
            builder.append("#### ADD Step 2\n\n").append(iteration.addStep2()).append("\n\n");
            builder.append("#### ADD Step 3\n\n").append(iteration.addStep3()).append("\n\n");
            builder.append("#### ADD Step 4\n\n").append(iteration.addStep4()).append("\n\n");
            builder.append("#### ADD Step 5\n\n").append(iteration.addStep5()).append("\n\n");
            builder.append("#### ADD Step 6\n\n").append(iteration.addStep6()).append("\n\n");
            builder.append("#### ADD Step 7\n\n").append(iteration.addStep7()).append("\n\n");
            builder.append("#### View Artifact\n\n");
            builder.append("```").append(iteration.diagramType()).append('\n');
            builder.append(iteration.diagramCode()).append("\n```\n\n");
        }

        builder.append("## 2. Interaction cost analysis\n\n");
        builder.append("The assignment was completed with the multi-agent paradigm. The workflow separated the design activity into analyst, architect, reviewer, diagram, and moderator roles so that the ADD outputs could be produced with explicit internal verification and traceable reasoning.\n\n");
        builder.append("- The way of completing the assignment: ").append(appProperties.getParadigm()).append('\n');
        builder.append("- The LLM used: ").append(appProperties.getLlmName()).append('\n');
        builder.append("- Number of Human Interactions (turns): ").append(result.interactionCost().humanInteractionTurns()).append('\n');
        builder.append("- Agent Turns: ").append(result.interactionCost().agentTurns()).append('\n');
        builder.append("- Token Consumption (K tokens): ").append(formatTokensInK(result.interactionCost().totalTokens())).append('\n');
        builder.append("- Time Cost (min): ").append(formatMinutes(result.interactionCost().elapsedSeconds())).append("\n\n");

        builder.append("## 3. Individual Reflection\n\n");
        builder.append("### 3.1 Problems encountered and solutions adopted\n\n");
        builder.append("- Problem: keeping the agents strictly within the provided prior knowledge while still producing concrete architectural decisions.\n");
        builder.append("- Solution adopted: the workflow used role prompts, dialogue rules, and reviewer checks that explicitly rejected unsupported assumptions and forced the outputs to remain traceable to the assignment drivers.\n");
        builder.append("- Problem: preserving consistency across four iterations while the design became more detailed.\n");
        builder.append("- Solution adopted: each iteration reused the summarized outputs of previous iterations so later decisions could refine the design instead of restarting it.\n");
        builder.append("- Problem: turning multi-agent output into material that is easy to submit and review.\n");
        builder.append("- Solution adopted: the system archived conversation logs, iteration snapshots, and a report draft so the team could validate and polish the result efficiently before submission.\n\n");
        builder.append("Replace or refine the points above so they reflect the actual issues your group observed during the final run.\n\n");
        builder.append("### 3.2 Personal contributions to the group work\n\n");
        builder.append("| Name (Chinese) | Contributions |\n");
        builder.append("| --- | --- |\n");
        if (request.getTeamMembers() == null || request.getTeamMembers().isEmpty()) {
            builder.append("| To be filled | To be filled |\n");
        }
        else {
            request.getTeamMembers().forEach(member -> builder.append("| ").append(member).append(" | To be filled |\n"));
        }

        builder.append("\n## Appendix A. Run Artifacts\n\n");
        builder.append("- Summary JSON: ").append(result.artifacts().summaryFile()).append('\n');
        builder.append("- Conversation logs: ").append(result.artifacts().conversationsDirectory()).append('\n');
        builder.append("- Iteration snapshots: ").append(result.artifacts().iterationsDirectory()).append('\n');
        builder.append("- Detailed report file: ").append(result.artifacts().reportFile()).append('\n');
        builder.append("- Recorded model turns: ").append(turns.size()).append('\n');

        return builder.toString();
    }

    private String joinList(List<String> items) {
        if (items == null || items.isEmpty()) {
            return "None";
        }
        return String.join(", ", items);
    }

    private String safeValue(String value) {
        return value == null || value.isBlank() ? "Not provided" : value;
    }

    private String formatMembers(List<String> members) {
        if (members == null || members.isEmpty()) {
            return "Not provided";
        }
        return members.stream()
                .filter(member -> member != null && !member.isBlank())
                .collect(Collectors.joining(", "));
    }

    private String formatTokensInK(Integer totalTokens) {
        if (totalTokens == null) {
            return "N/A";
        }
        return String.format(Locale.ROOT, "%.2f", totalTokens / 1000.0);
    }

    private String formatMinutes(long elapsedSeconds) {
        return String.format(Locale.ROOT, "%.2f", elapsedSeconds / 60.0);
    }
}
