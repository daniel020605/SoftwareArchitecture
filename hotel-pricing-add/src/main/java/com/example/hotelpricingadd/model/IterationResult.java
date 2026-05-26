package com.example.hotelpricingadd.model;

import java.util.List;

public record IterationResult(
        int iterationNumber,
        String iterationTitle,
        String iterationGoal,
        List<String> selectedDrivers,
        String addStep2,
        String addStep3,
        String addStep4,
        String addStep5,
        String addStep6,
        String addStep7,
        String diagramType,
        String diagramCode,
        List<String> keyDecisions,
        List<String> followUpRisks) {
}
