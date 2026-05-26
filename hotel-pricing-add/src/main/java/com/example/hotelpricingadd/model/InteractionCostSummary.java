package com.example.hotelpricingadd.model;

public record InteractionCostSummary(
        int agentTurns,
        int humanInteractionTurns,
        Integer promptTokens,
        Integer completionTokens,
        Integer totalTokens,
        long elapsedSeconds) {
}
