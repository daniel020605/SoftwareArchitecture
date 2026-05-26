package com.example.hotelpricingadd.model;

import java.time.Instant;

public record AgentTurn(
        Instant timestamp,
        AgentRole role,
        int iterationNumber,
        String iterationTitle,
        String systemPrompt,
        String userPrompt,
        String response,
        TokenUsageSnapshot usage,
        long latencyMillis) {
}
