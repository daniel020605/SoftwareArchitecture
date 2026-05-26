package com.example.hotelpricingadd.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class AssignmentKnowledgeService {

    private final String addMethod;
    private final String hotelPricingSystem;
    private final String iterationPlan;

    public AssignmentKnowledgeService() {
        this.addMethod = readResource("knowledge/add-3.0.md");
        this.hotelPricingSystem = readResource("knowledge/hotel-pricing-system.md");
        this.iterationPlan = readResource("knowledge/iteration-plan.md");
    }

    public String addMethod() {
        return addMethod;
    }

    public String hotelPricingSystem() {
        return hotelPricingSystem;
    }

    public String iterationPlan() {
        return iterationPlan;
    }

    public String priorKnowledgeBundle() {
        return """
                ## Prior knowledge bundle

                ### ADD 3.0
                %s

                ### Hotel Pricing System
                %s

                ### Fixed iteration plan
                %s
                """.formatted(addMethod, hotelPricingSystem, iterationPlan);
    }

    private String readResource(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
        catch (IOException ex) {
            throw new IllegalStateException("Failed to load resource: " + path, ex);
        }
    }
}
