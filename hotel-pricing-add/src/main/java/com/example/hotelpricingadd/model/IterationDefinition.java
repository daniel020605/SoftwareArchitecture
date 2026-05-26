package com.example.hotelpricingadd.model;

import java.util.List;

public record IterationDefinition(
        int number,
        String title,
        String focus,
        List<String> suggestedDrivers,
        List<String> expectedOutputs) {
}
