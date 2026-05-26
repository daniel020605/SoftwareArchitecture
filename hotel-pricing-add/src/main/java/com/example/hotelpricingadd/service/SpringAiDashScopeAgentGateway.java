package com.example.hotelpricingadd.service;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import com.example.hotelpricingadd.model.AgentInvocationRequest;
import com.example.hotelpricingadd.model.AgentTurn;
import com.example.hotelpricingadd.model.TokenUsageSnapshot;

public class SpringAiDashScopeAgentGateway implements AgentGateway {

    private final ChatModel chatModel;

    public SpringAiDashScopeAgentGateway(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public AgentTurn invoke(AgentInvocationRequest request) {
        Instant timestamp = Instant.now();
        long started = System.nanoTime();
        Prompt prompt = new Prompt(List.of(
                new SystemMessage(request.systemPrompt()),
                new UserMessage(request.userPrompt())));
        ChatResponse response = chatModel.call(prompt);
        long latencyMillis = (System.nanoTime() - started) / 1_000_000;
        return new AgentTurn(
                timestamp,
                request.role(),
                request.iterationNumber(),
                request.iterationTitle(),
                request.systemPrompt(),
                request.userPrompt(),
                extractText(response),
                extractUsage(response),
                latencyMillis);
    }

    private String extractText(ChatResponse response) {
        if (response == null || response.getResult() == null || response.getResult().getOutput() == null) {
            return "";
        }
        String text = response.getResult().getOutput().getText();
        return text == null ? "" : text;
    }

    private TokenUsageSnapshot extractUsage(ChatResponse response) {
        Object metadata = response == null ? null : response.getMetadata();
        Object usage = invokeGetter(metadata, "getUsage");
        if (usage == null) {
            return TokenUsageSnapshot.empty();
        }

        Integer promptTokens = firstInteger(usage, "getPromptTokens", "getInputTokens");
        Integer completionTokens = firstInteger(usage, "getCompletionTokens", "getGenerationTokens", "getOutputTokens");
        Integer totalTokens = firstInteger(usage, "getTotalTokens");
        return new TokenUsageSnapshot(promptTokens, completionTokens, totalTokens);
    }

    private Object invokeGetter(Object source, String methodName) {
        if (source == null) {
            return null;
        }
        try {
            Method method = source.getClass().getMethod(methodName);
            return method.invoke(source);
        }
        catch (ReflectiveOperationException ex) {
            return null;
        }
    }

    private Integer firstInteger(Object source, String... methodNames) {
        for (String methodName : methodNames) {
            Object value = invokeGetter(source, methodName);
            if (value instanceof Number number) {
                return number.intValue();
            }
        }
        return null;
    }
}
