package com.example.hotelpricingadd.service;

import java.util.List;

import com.example.hotelpricingadd.model.IterationDefinition;

public final class AssignmentCatalog {

    private AssignmentCatalog() {
    }

    public static List<IterationDefinition> iterations() {
        return List.of(
                new IterationDefinition(
                        1,
                        "Establishing an Overall System Structure",
                        "Create the initial architectural decomposition, system context, major containers, and the first set of interfaces for the greenfield system.",
                        List.of("CRN-1", "CRN-2", "CON-1", "CON-2", "CON-4", "CON-5", "CON-6", "QA-3", "QA-4", "QA-5", "QA-7"),
                        List.of("System context", "High-level container view", "Responsibility allocation", "Initial deployment assumptions")),
                new IterationDefinition(
                        2,
                        "Identifying Structures to Support Primary Functionality",
                        "Refine the structures that directly support login, price changes, price queries, hotel management, rate management, and user management.",
                        List.of("HPS-1", "HPS-2", "HPS-3", "HPS-4", "HPS-5", "HPS-6", "QA-1", "QA-5", "QA-6"),
                        List.of("Runtime collaboration view", "Service interfaces", "Data ownership", "Simulation and publication flow")),
                new IterationDefinition(
                        3,
                        "Addressing Reliability and Availability Quality Attributes",
                        "Refine the design to satisfy reliability, availability, scalability, monitorability, and testability scenarios without violating the earlier structure.",
                        List.of("QA-2", "QA-3", "QA-4", "QA-8", "QA-9", "CON-5", "CON-6", "CRN-4"),
                        List.of("Failure handling strategy", "Availability-oriented structure", "Observability hooks", "Test seams")),
                new IterationDefinition(
                        4,
                        "Addressing Development and Operations",
                        "Refine development, deployment, delivery, team allocation, and operational structures to support the MVP and the six-month release goal.",
                        List.of("QA-7", "QA-8", "QA-9", "CRN-3", "CRN-5", "CON-3", "CON-4", "CON-6"),
                        List.of("Environment strategy", "Delivery pipeline", "Team ownership boundaries", "Operational support view")));
    }
}
