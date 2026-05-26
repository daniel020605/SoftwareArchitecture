package com.example.hotelpricingadd.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotelpricingadd.config.AppProperties;
import com.example.hotelpricingadd.model.AssignmentRunResult;
import com.example.hotelpricingadd.model.RunRequest;
import com.example.hotelpricingadd.service.AddMultiAgentWorkflowService;
import com.example.hotelpricingadd.service.AssignmentCatalog;
import com.example.hotelpricingadd.service.AssignmentKnowledgeService;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/add")
public class AssignmentRunController {

    private final AddMultiAgentWorkflowService workflowService;
    private final AssignmentKnowledgeService knowledgeService;
    private final AppProperties appProperties;

    public AssignmentRunController(
            AddMultiAgentWorkflowService workflowService,
            AssignmentKnowledgeService knowledgeService,
            AppProperties appProperties) {
        this.workflowService = workflowService;
        this.knowledgeService = knowledgeService;
        this.appProperties = appProperties;
    }

    @GetMapping("/brief")
    public Map<String, Object> brief() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("assignmentName", appProperties.getAssignmentName());
        payload.put("paradigm", appProperties.getParadigm());
        payload.put("llmName", appProperties.getLlmName());
        payload.put("iterations", AssignmentCatalog.iterations());
        payload.put("priorKnowledge", Map.of(
                "addMethod", knowledgeService.addMethod(),
                "hotelPricingSystem", knowledgeService.hotelPricingSystem(),
                "iterationPlan", knowledgeService.iterationPlan()));
        return payload;
    }

    @PostMapping("/runs")
    public AssignmentRunResult run(@Valid @RequestBody(required = false) RunRequest request) {
        RunRequest effectiveRequest = request == null ? new RunRequest() : request;
        return workflowService.run(effectiveRequest);
    }
}
