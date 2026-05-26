package com.example.hotelpricingadd.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.example.hotelpricingadd.service.AgentGateway;
import com.example.hotelpricingadd.service.SpringAiDashScopeAgentGateway;
import com.example.hotelpricingadd.service.UnavailableAgentGateway;

@Configuration
public class AgentGatewayConfiguration {

    @Bean
    public AgentGateway dashScopeAgentGateway(@Lazy ChatModel chatModel) {
        return new SpringAiDashScopeAgentGateway(chatModel);
    }

    @Bean
    @ConditionalOnMissingBean(AgentGateway.class)
    public AgentGateway unavailableAgentGateway() {
        return new UnavailableAgentGateway();
    }
}
