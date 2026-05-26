package com.example.hotelpricingadd.service;

import com.example.hotelpricingadd.model.AgentInvocationRequest;
import com.example.hotelpricingadd.model.AgentTurn;

public class UnavailableAgentGateway implements AgentGateway {

    @Override
    public AgentTurn invoke(AgentInvocationRequest request) {
        throw new IllegalStateException("""
                DashScope chat model is not available.
                Set SPRING_AI_DASHSCOPE_ENABLED=true and provide AI_DASHSCOPE_API_KEY (or DASHSCOPE_API_KEY) before running the multi-agent workflow.
                """.strip());
    }
}
