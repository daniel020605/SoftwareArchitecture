package com.example.hotelpricingadd.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class RunRequest {

    @Size(max = 120)
    private String teamName;

    private List<@Size(max = 80) String> teamMembers = new ArrayList<>();

    @Size(max = 20)
    private String diagramType;

    @Min(0)
    private Integer humanInteractionTurns;

    @Min(1)
    @Max(3)
    private Integer reviewerRounds;

    @Size(max = 2000)
    private String notes;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<String> teamMembers) {
        this.teamMembers = teamMembers == null ? new ArrayList<>() : teamMembers;
    }

    public String getDiagramType() {
        return diagramType;
    }

    public void setDiagramType(String diagramType) {
        this.diagramType = diagramType;
    }

    public Integer getHumanInteractionTurns() {
        return humanInteractionTurns;
    }

    public void setHumanInteractionTurns(Integer humanInteractionTurns) {
        this.humanInteractionTurns = humanInteractionTurns;
    }

    public Integer getReviewerRounds() {
        return reviewerRounds;
    }

    public void setReviewerRounds(Integer reviewerRounds) {
        this.reviewerRounds = reviewerRounds;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
