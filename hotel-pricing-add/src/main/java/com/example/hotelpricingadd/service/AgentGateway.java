package com.example.hotelpricingadd.service;

import com.example.hotelpricingadd.model.AgentInvocationRequest;
import com.example.hotelpricingadd.model.AgentTurn;

public interface AgentGateway {

    AgentTurn invoke(AgentInvocationRequest request);
}
