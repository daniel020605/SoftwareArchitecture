package com.example.hotelpricingadd.model;

public enum AgentRole {
    REQUIREMENTS_ANALYST("Requirements Analyst"),
    SOLUTION_ARCHITECT("Solution Architect"),
    QUALITY_REVIEWER("Quality Reviewer"),
    DIAGRAM_CURATOR("Diagram Curator"),
    ITERATION_MODERATOR("Iteration Moderator");

    private final String displayName;

    AgentRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
