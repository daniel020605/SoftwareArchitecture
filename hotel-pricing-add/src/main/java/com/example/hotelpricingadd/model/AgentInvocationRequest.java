package com.example.hotelpricingadd.model;

public record AgentInvocationRequest(
        AgentRole role,
        int iterationNumber,
        String iterationTitle,
        String systemPrompt,
        String userPrompt) {
}
