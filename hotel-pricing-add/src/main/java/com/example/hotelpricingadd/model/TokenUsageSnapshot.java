package com.example.hotelpricingadd.model;

public record TokenUsageSnapshot(
        Integer promptTokens,
        Integer completionTokens,
        Integer totalTokens) {

    public static TokenUsageSnapshot empty() {
        return new TokenUsageSnapshot(null, null, null);
    }
}
