package com.example.hotelpricingadd.config;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String assignmentName = "Software Architecture (2026) Assignment 2";
    private String paradigm = "C: Multi-agent";
    private String llmName = "Qwen3-Max";
    private String outputLanguage = "English";
    private Path artifactRoot = Path.of("./artifacts");
    private String defaultDiagramType = "mermaid";
    private int reviewerRounds = 1;

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getParadigm() {
        return paradigm;
    }

    public void setParadigm(String paradigm) {
        this.paradigm = paradigm;
    }

    public String getLlmName() {
        return llmName;
    }

    public void setLlmName(String llmName) {
        this.llmName = llmName;
    }

    public String getOutputLanguage() {
        return outputLanguage;
    }

    public void setOutputLanguage(String outputLanguage) {
        this.outputLanguage = outputLanguage;
    }

    public Path getArtifactRoot() {
        return artifactRoot;
    }

    public void setArtifactRoot(Path artifactRoot) {
        this.artifactRoot = artifactRoot;
    }

    public String getDefaultDiagramType() {
        return defaultDiagramType;
    }

    public void setDefaultDiagramType(String defaultDiagramType) {
        this.defaultDiagramType = defaultDiagramType;
    }

    public int getReviewerRounds() {
        return reviewerRounds;
    }

    public void setReviewerRounds(int reviewerRounds) {
        this.reviewerRounds = reviewerRounds;
    }
}
