package com.example.hotelpricingadd.service;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.hotelpricingadd.config.AppProperties;
import com.example.hotelpricingadd.model.AgentInvocationRequest;
import com.example.hotelpricingadd.model.AgentRole;
import com.example.hotelpricingadd.model.IterationDefinition;
import com.example.hotelpricingadd.model.IterationResult;
import com.example.hotelpricingadd.model.RunRequest;

@Component
public class PromptFactory {

    private final AssignmentKnowledgeService knowledgeService;
    private final AppProperties appProperties;

    public PromptFactory(AssignmentKnowledgeService knowledgeService, AppProperties appProperties) {
        this.knowledgeService = knowledgeService;
        this.appProperties = appProperties;
    }

    public AgentInvocationRequest analystRequest(IterationDefinition iteration, List<IterationResult> history, RunRequest request) {
        return new AgentInvocationRequest(
                AgentRole.REQUIREMENTS_ANALYST,
                iteration.number(),
                iteration.title(),
                baseSystemPrompt(iteration, """
                        Role: Requirements Analyst
                        Mission:
                        - Select the iteration drivers grounded only in the provided prior knowledge.
                        - Frame the ADD Step 2 and Step 3 scope for the rest of the agent team.
                        - Identify the minimum design concepts that must be evaluated next.

                        Dialogue rules:
                        - Cite driver IDs exactly, such as HPS-2, QA-3, CRN-1, or CON-6.
                        - Do not invent technologies, patterns, or requirements that are not explicitly derivable from the prior knowledge.
                        - If evidence is missing, state "Insufficient evidence in prior knowledge".
                        - Stop before final design instantiation; your job is to shape the design space, not to finalize it.

                        Output format:
                        Return concise English Markdown with these headings:
                        1. Selected drivers
                        2. Iteration goal
                        3. Elements to refine
                        4. Candidate design concepts
                        5. Risks or ambiguities
                        """),
                """
                        Iteration focus:
                        %s

                        Suggested driver candidates:
                        %s

                        Expected outputs:
                        %s

                        Prior iteration context:
                        %s

                        Team context:
                        %s

                        Additional operator notes:
                        %s
                        """.formatted(
                        iteration.focus(),
                        joinList(iteration.suggestedDrivers()),
                        joinList(iteration.expectedOutputs()),
                        historyContext(history),
                        teamContext(request),
                        safeNotes(request.getNotes())));
    }

    public AgentInvocationRequest architectDraftRequest(
            IterationDefinition iteration,
            List<IterationResult> history,
            String analystOutput,
            RunRequest request) {
        return new AgentInvocationRequest(
                AgentRole.SOLUTION_ARCHITECT,
                iteration.number(),
                iteration.title(),
                baseSystemPrompt(iteration, """
                        Role: Solution Architect
                        Mission:
                        - Use the analyst brief to propose the architecture for ADD Step 4, Step 5, and Step 6.
                        - Keep the design feasible for a greenfield system under the stated constraints.
                        - Prefer clear module and interface boundaries that can be allocated to a team.

                        Dialogue rules:
                        - Use only the prior knowledge bundle and the analyst's output.
                        - You may reference Java, Angular, Kafka, REST, cloud provider identity service, proprietary Git platform, and cloud-native deployment because they appear in the prior knowledge bundle.
                        - Do not cite external standards, products, protocol names, infrastructure products, or pattern names that are absent from the prior knowledge.
                        - When a concept is needed but a named pattern is not present in the prior knowledge, describe the responsibility generically.
                        - Make reasoning explicit enough for later review.

                        Output format:
                        Return English Markdown with these headings:
                        1. Step 4 design concepts and trade-offs
                        2. Step 5 elements, responsibilities, and interfaces
                        3. Step 6 key views to preserve
                        4. Step 6 major decisions and rationale
                        5. Remaining risks
                        """),
                """
                        Iteration focus:
                        %s

                        Prior iteration context:
                        %s

                        Analyst brief:
                        %s

                        Diagram target:
                        %s

                        Team context:
                        %s
                        """.formatted(
                        iteration.focus(),
                        historyContext(history),
                        analystOutput,
                        resolvedDiagramType(request),
                        teamContext(request)));
    }

    public AgentInvocationRequest reviewerRequest(
            IterationDefinition iteration,
            List<IterationResult> history,
            String analystOutput,
            String architectDraft) {
        return new AgentInvocationRequest(
                AgentRole.QUALITY_REVIEWER,
                iteration.number(),
                iteration.title(),
                baseSystemPrompt(iteration, """
                        Role: Quality Reviewer
                        Mission:
                        - Review the architectural proposal for compliance with the selected drivers, the assignment constraints, and the fixed iteration goal.
                        - Perform collaborative verification without adding new external knowledge.
                        - Decide whether the design is ready to consolidate or needs a targeted revision.

                        Dialogue rules:
                        - Review against the prior knowledge bundle only.
                        - Flag any invented requirement, unsupported assumption, or violation of the iteration scope.
                        - Keep feedback actionable and traceable to driver IDs.

                        Output format:
                        Return English Markdown with these headings:
                        1. Review verdict (ACCEPT, ACCEPT_WITH_MINOR_FIXES, or REVISE)
                        2. Evidence-based strengths
                        3. Gaps or violations
                        4. Required revision actions
                        """),
                """
                        Iteration focus:
                        %s

                        Prior iteration context:
                        %s

                        Analyst brief:
                        %s

                        Architect draft:
                        %s
                        """.formatted(
                        iteration.focus(),
                        historyContext(history),
                        analystOutput,
                        architectDraft));
    }

    public AgentInvocationRequest architectRevisionRequest(
            IterationDefinition iteration,
            List<IterationResult> history,
            String architectDraft,
            String reviewOutput) {
        return new AgentInvocationRequest(
                AgentRole.SOLUTION_ARCHITECT,
                iteration.number(),
                iteration.title(),
                baseSystemPrompt(iteration, """
                        Role: Solution Architect
                        Mission:
                        - Produce the revised architectural response after collaborative review.
                        - Preserve valid content from the draft and address each reviewer action explicitly.
                        - Keep the response ready for diagram extraction and structured consolidation.

                        Dialogue rules:
                        - Use only the draft, the review feedback, and the prior knowledge bundle.
                        - Do not ignore reviewer findings.
                        - Keep the language concise and implementation-oriented.

                        Output format:
                        Return English Markdown with these headings:
                        1. Revision summary
                        2. Final Step 4 design concepts
                        3. Final Step 5 elements, responsibilities, and interfaces
                        4. Final Step 6 decisions and rationale
                        5. Residual risks
                        """),
                """
                        Iteration focus:
                        %s

                        Prior iteration context:
                        %s

                        Original architect draft:
                        %s

                        Reviewer feedback:
                        %s
                        """.formatted(
                        iteration.focus(),
                        historyContext(history),
                        architectDraft,
                        reviewOutput));
    }

    public AgentInvocationRequest diagramRequest(
            IterationDefinition iteration,
            String architectRevision,
            RunRequest request) {
        String diagramType = resolvedDiagramType(request);
        return new AgentInvocationRequest(
                AgentRole.DIAGRAM_CURATOR,
                iteration.number(),
                iteration.title(),
                baseSystemPrompt(iteration, """
                        Role: Diagram Curator
                        Mission:
                        - Convert the approved architecture into a single diagram artifact.
                        - Keep the diagram aligned with the architectural elements and interfaces already agreed by the other agents.
                        - Emit diagram code that can be copied into tooling without manual cleanup.

                        Dialogue rules:
                        - Use only the approved architecture text and the prior knowledge bundle.
                        - Do not introduce new elements in the diagram.
                        - Keep the code block self-contained.
                        %s

                        Output format:
                        Return English Markdown with these headings:
                        1. Diagram rationale
                        2. Diagram code
                        The diagram code section must contain exactly one fenced %s block.
                        """.formatted(diagramSyntaxRule(diagramType), diagramType)),
                """
                        Iteration focus:
                        %s

                        Approved architecture:
                        %s
                        """.formatted(
                        iteration.focus(),
                        architectRevision));
    }

    public AgentInvocationRequest moderatorRequest(
            IterationDefinition iteration,
            String analystOutput,
            String architectRevision,
            String reviewOutput,
            String diagramOutput,
            RunRequest request) {
        String diagramType = resolvedDiagramType(request);
        return new AgentInvocationRequest(
                AgentRole.ITERATION_MODERATOR,
                iteration.number(),
                iteration.title(),
                baseSystemPrompt(iteration, """
                        Role: Iteration Moderator
                        Mission:
                        - Consolidate the multi-agent discussion into the final ADD iteration result.
                        - Preserve traceability to ADD Step 2 through Step 7.
                        - Produce machine-readable JSON so the system can archive the result and generate the report.

                        Dialogue rules:
                        - Use only the prior knowledge bundle and the outputs from the other agents.
                        - Do not invent new architectural content during consolidation.
                        - For Mermaid output, preserve only valid graph syntax beginning with "flowchart" or "graph"; do not emit non-graph notation.
                        - Keep all prose in English.

                        Output format:
                        Return valid JSON only. No code fences. Use this exact schema:
                        {
                          "iterationNumber": %d,
                          "iterationTitle": "%s",
                          "iterationGoal": "string",
                          "selectedDrivers": ["string"],
                          "addStep2": "string",
                          "addStep3": "string",
                          "addStep4": "string",
                          "addStep5": "string",
                          "addStep6": "string",
                          "addStep7": "string",
                          "diagramType": "%s",
                          "diagramCode": "string",
                          "keyDecisions": ["string"],
                          "followUpRisks": ["string"]
                        }
                        """.formatted(iteration.number(), escapeJsonText(iteration.title()), diagramType)),
                """
                        Iteration focus:
                        %s

                        Analyst output:
                        %s

                        Final architect output:
                        %s

                        Reviewer output:
                        %s

                        Diagram output:
                        %s
                        """.formatted(
                        iteration.focus(),
                        analystOutput,
                        architectRevision,
                        reviewOutput,
                        diagramOutput));
    }

    private String baseSystemPrompt(IterationDefinition iteration, String roleInstructions) {
        return """
                You are part of a multi-agent workflow for %s.

                Assignment selection:
                - Paradigm: %s
                - LLM baseline: %s
                - Output language: %s

                Fixed workflow:
                1. Requirements Analyst frames the drivers and scope.
                2. Solution Architect proposes the design.
                3. Quality Reviewer verifies compliance.
                4. Solution Architect revises the design.
                5. Diagram Curator preserves the agreed view.
                6. Iteration Moderator consolidates the iteration result.

                Hard assignment rules:
                - Use only the provided prior knowledge.
                - No external domain knowledge, few-shot examples, or handcrafted demonstrations.
                - No requirement augmentation beyond the prior knowledge bundle.
                - All decisions must be explicitly grounded in the system instructions and the prior knowledge.
                - Do not use named external standards, products, protocols, or pattern labels unless the exact name appears in the prior knowledge bundle.
                - Views must be represented in Mermaid or PlantUML code.

                Current iteration:
                - Title: %s
                - Focus: %s

                %s

                %s
                """.formatted(
                appProperties.getAssignmentName(),
                appProperties.getParadigm(),
                appProperties.getLlmName(),
                appProperties.getOutputLanguage(),
                iteration.title(),
                iteration.focus(),
                roleInstructions,
                knowledgeService.priorKnowledgeBundle());
    }

    private String historyContext(List<IterationResult> history) {
        if (history == null || history.isEmpty()) {
            return "No previous iterations are available yet.";
        }

        return history.stream()
                .map(result -> """
                        Iteration %d - %s
                        Goal: %s
                        Drivers: %s
                        Key decisions: %s
                        Remaining risks: %s
                        """.formatted(
                        result.iterationNumber(),
                        result.iterationTitle(),
                        result.iterationGoal(),
                        joinList(result.selectedDrivers()),
                        joinList(result.keyDecisions()),
                        joinList(result.followUpRisks())))
                .collect(Collectors.joining("\n"));
    }

    private String teamContext(RunRequest request) {
        StringJoiner joiner = new StringJoiner(", ");
        if (request.getTeamMembers() != null) {
            request.getTeamMembers().stream()
                    .filter(member -> member != null && !member.isBlank())
                    .forEach(joiner::add);
        }
        String members = joiner.length() == 0 ? "Not provided" : joiner.toString();
        String teamName = request.getTeamName() == null || request.getTeamName().isBlank() ? "Not provided" : request.getTeamName();
        return "Team name: " + teamName + "; members: " + members;
    }

    private String safeNotes(String notes) {
        return notes == null || notes.isBlank() ? "None." : notes;
    }

    public String resolvedDiagramType(RunRequest request) {
        if (request.getDiagramType() == null || request.getDiagramType().isBlank()) {
            return appProperties.getDefaultDiagramType();
        }
        return request.getDiagramType().trim().toLowerCase();
    }

    private String diagramSyntaxRule(String diagramType) {
        if ("mermaid".equalsIgnoreCase(diagramType)) {
            return """
                    - Mermaid diagrams must use valid graph syntax and start with either "flowchart TD", "flowchart LR", "graph TD", or "graph LR".
                    - Do not use non-graph notation, UML-style syntax, note blocks, or HTML tags inside Mermaid.
                    - Represent components as ordinary graph nodes and interfaces as labeled edges or ordinary nodes.
                    """;
        }
        if ("plantuml".equalsIgnoreCase(diagramType)) {
            return "- PlantUML diagrams must be complete between @startuml and @enduml.";
        }
        return "- Use a diagram syntax that can be rendered without manual cleanup.";
    }

    private String joinList(List<String> items) {
        if (items == null || items.isEmpty()) {
            return "None";
        }
        return String.join(", ", items);
    }

    private String escapeJsonText(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
