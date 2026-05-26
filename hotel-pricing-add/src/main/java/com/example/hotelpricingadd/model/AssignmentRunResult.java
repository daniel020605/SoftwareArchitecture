package com.example.hotelpricingadd.model;

import java.time.Instant;
import java.util.List;

public record AssignmentRunResult(
        String runId,
        String assignmentName,
        String paradigm,
        String llmName,
        Instant startedAt,
        Instant finishedAt,
        InteractionCostSummary interactionCost,
        List<IterationResult> iterations,
        ArtifactLocations artifacts) {
}
