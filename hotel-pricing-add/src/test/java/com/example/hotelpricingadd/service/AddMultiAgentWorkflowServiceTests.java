package com.example.hotelpricingadd.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.example.hotelpricingadd.config.AppProperties;
import com.example.hotelpricingadd.model.AgentInvocationRequest;
import com.example.hotelpricingadd.model.AgentRole;
import com.example.hotelpricingadd.model.AgentTurn;
import com.example.hotelpricingadd.model.AssignmentRunResult;
import com.example.hotelpricingadd.model.RunRequest;
import com.example.hotelpricingadd.model.TokenUsageSnapshot;
import com.fasterxml.jackson.databind.ObjectMapper;

class AddMultiAgentWorkflowServiceTests {

    @TempDir
    Path tempDir;

    @Test
    void runProducesArtifactsForAllIterations() throws Exception {
        AppProperties properties = new AppProperties();
        properties.setArtifactRoot(tempDir);

        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        AssignmentKnowledgeService knowledgeService = new AssignmentKnowledgeService();
        PromptFactory promptFactory = new PromptFactory(knowledgeService, properties);
        ReportComposer reportComposer = new ReportComposer(properties);
        ArtifactService artifactService = new ArtifactService(properties, objectMapper, reportComposer);
        AgentGateway stubGateway = new StubAgentGateway();

        AddMultiAgentWorkflowService workflowService =
                new AddMultiAgentWorkflowService(stubGateway, promptFactory, artifactService, properties, objectMapper);

        RunRequest request = new RunRequest();
        request.setTeamName("Team Alpha");
        request.setTeamMembers(List.of("Alice", "Bob", "Carol"));

        AssignmentRunResult result = workflowService.run(request);

        assertThat(result.iterations()).hasSize(4);
        assertThat(Files.exists(Path.of(result.artifacts().reportFile()))).isTrue();
        assertThat(Files.exists(Path.of(result.artifacts().summaryFile()))).isTrue();
        assertThat(Files.exists(Path.of(result.artifacts().conversationsDirectory(), "iteration-01.md"))).isTrue();
        assertThat(Files.exists(Path.of(result.artifacts().iterationsDirectory(), "iteration-04.json"))).isTrue();
        assertThat(result.interactionCost().agentTurns()).isEqualTo(24);
    }

    private static final class StubAgentGateway implements AgentGateway {

        @Override
        public AgentTurn invoke(AgentInvocationRequest request) {
            String response = switch (request.role()) {
                case REQUIREMENTS_ANALYST -> """
                        ## Selected drivers
                        - QA-1
                        - QA-3

                        ## Iteration goal
                        Deliver the scoped drivers.

                        ## Elements to refine
                        - API Gateway

                        ## Candidate design concepts
                        - Service decomposition

                        ## Risks or ambiguities
                        - None
                        """;
                case SOLUTION_ARCHITECT -> """
                        ## Revision summary
                        The draft is aligned.

                        ## Final Step 4 design concepts
                        Service decomposition and explicit interface boundaries.

                        ## Final Step 5 elements, responsibilities, and interfaces
                        Pricing API, pricing core, publication adapter.

                        ## Final Step 6 decisions and rationale
                        Keep boundaries explicit for later change.

                        ## Residual risks
                        None
                        """;
                case QUALITY_REVIEWER -> """
                        ## Review verdict
                        ACCEPT_WITH_MINOR_FIXES

                        ## Evidence-based strengths
                        The design matches the iteration focus.

                        ## Gaps or violations
                        None

                        ## Required revision actions
                        Tighten the wording.
                        """;
                case DIAGRAM_CURATOR -> """
                        ## Diagram rationale
                        Preserve the key runtime view.

                        ## Diagram code
                        ```mermaid
                        graph TD
                            UI[Angular UI] --> API[Pricing API]
                            API --> CORE[Pricing Core]
                            CORE --> CMS[Channel Management Adapter]
                        ```
                        """;
                case ITERATION_MODERATOR -> """
                        {
                          "iterationNumber": %d,
                          "iterationTitle": "%s",
                          "iterationGoal": "Iteration %d goal.",
                          "selectedDrivers": ["QA-1", "QA-3", "CON-6"],
                          "addStep2": "Drivers were selected for iteration %d.",
                          "addStep3": "The system elements to refine were scoped for iteration %d.",
                          "addStep4": "The team selected service decomposition for iteration %d.",
                          "addStep5": "Responsibilities and interfaces were allocated for iteration %d.",
                          "addStep6": "The design decisions and views were recorded for iteration %d.",
                          "addStep7": "The design satisfied the iteration goal for iteration %d.",
                          "diagramType": "mermaid",
                          "diagramCode": "graph TD\\n    UI[Angular UI] --> API[Pricing API]\\n    API --> CORE[Pricing Core]\\n    CORE --> CMS[Channel Management Adapter]",
                          "keyDecisions": ["Use explicit service boundaries", "Preserve integration seams"],
                          "followUpRisks": ["Refine details in the next iteration"]
                        }
                        """.formatted(
                        request.iterationNumber(),
                        request.iterationTitle().replace("\"", "\\\""),
                        request.iterationNumber(),
                        request.iterationNumber(),
                        request.iterationNumber(),
                        request.iterationNumber(),
                        request.iterationNumber(),
                        request.iterationNumber(),
                        request.iterationNumber());
            };

            return new AgentTurn(
                    Instant.now(),
                    request.role(),
                    request.iterationNumber(),
                    request.iterationTitle(),
                    request.systemPrompt(),
                    request.userPrompt(),
                    response,
                    new TokenUsageSnapshot(10, 20, 30),
                    25L);
        }
    }
}
