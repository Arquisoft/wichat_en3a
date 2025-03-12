package com.uniovi.hintservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LLMService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String setupMessage = "You are part of a web game about questions and answers. You will be in charge of giving clues " +
            "to the player. You will receive a question and an answer from now on in the following format <question>;<answer> " +
            "when you receive that message you will reply with a clue taking in to account both the question and the answer." +
            "Your replies must have the following characteristics: They must be as short as posible, they will consist of just a" +
            "clue with no additional information, the clues must not contain the answer in them. Example of expected response\n" +
            "you receive: 'What is the capital of France;Paris' \n" +
            "posible answer: 'In the capital of france you can visit the Eiffel Tower' ";
    private static final Map<String, Boolean> modelSetupTracker = new HashMap<>();
    private static final Map<String, LLMConfig> llmConfigs = new HashMap<>();

    @Value("${llm.api.key}")
    private String apiKey;
    private final String model = "gemini";

    public LLMService() {
        llmConfigs.put("gemini", new LLMConfig(
                (apiKey) -> "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey,
                (question, answer, model) -> {
                    ObjectNode request = objectMapper.createObjectNode();
                    ArrayNode messages = objectMapper.createArrayNode();

                    if (!modelSetupTracker.getOrDefault(model, false)) {
                        ObjectNode setupNode = objectMapper.createObjectNode();
                        setupNode.put("role", "system");
                        setupNode.put("content", setupMessage);
                        messages.add(setupNode);
                        modelSetupTracker.put(model, true);
                    }

                    ObjectNode userMessage = objectMapper.createObjectNode();
                    userMessage.put("role", "user");
                    userMessage.put("content", question + ";" + answer);
                    messages.add(userMessage);

                    request.set("messages", messages);
                    return request;
                },
                (response) -> {
                    JsonNode choices = response.path("data").path("candidates");
                    if (choices.isEmpty()) {
                        return "There was an unexpected problem: No response from LLM model.";
                    }
                    return choices.get(0).path("content").path("parts").get(0).path("text").asText();
                }
        ));
    }

    public String askLLM(String question, String answer) {
        try {
            LLMConfig config = llmConfigs.get(model);
            if (config == null) {
                throw new RuntimeException("No LLM model found for " + model);
            }

            String url = config.url.apply(apiKey);
            ObjectNode requestData = config.transformRequest.apply(question, answer, model);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestData.toString(), headers);
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, requestEntity, JsonNode.class);

            if (response.getBody() != null) {
                return config.transformResponse.apply(response.getBody());
            } else {
                throw new RuntimeException("Empty response from " + model + " API.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch response from model: " + e.getMessage());
        }
    }

    private static class LLMConfig {
        public Function<String, String> url;
        public TriFunction<String, String, String, ObjectNode> transformRequest;
        public Function<JsonNode, String> transformResponse;

        public LLMConfig(Function<String, String> url,
                         TriFunction<String, String, String, ObjectNode> transformRequest,
                         Function<JsonNode, String> transformResponse) {
            this.url = url;
            this.transformRequest = transformRequest;
            this.transformResponse = transformResponse;
        }
    }

    @FunctionalInterface
    interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }

}
