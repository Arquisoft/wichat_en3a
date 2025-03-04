package com.uniovi.hintservice.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class LLMService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, Boolean> modelSetupTracker = new HashMap<>();

    private static final String SETUP_MESSAGE = "You are part of a web game about questions and answers. You will be in charge of giving clues " +
            "to the player. You will receive a question and an answer from now on in the following format <question>;<answer> " +
            "when you receive that message you will reply with a clue taking into account both the question and the answer. " +
            "Your replies must be as short as possible, they will consist of just a " +
            "clue with no additional information, the clues must not contain the answer in them.";

    public String askLLM(String question, String answer, String apiKey, String model){
        LlmConfig config = new LlmConfigFactory().getConfig(model);
        if (config == null) {
            return "Unsupported model: " + model;
        }

        String url = config.getUrl(apiKey);
        String requestBody = config.transformRequest(question, answer, model, modelSetupTracker);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.addAll(config.getHeaders(apiKey));

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        return config.transformResponse(responseEntity.getBody());
    }

    interface LlmConfig {
        String getUrl(String apiKey);
        String transformRequest(String question, String answer, String model, Map<String, Boolean> modelSetupTracker);
        String transformResponse(String response);
        HttpHeaders getHeaders(String apiKey);
    }

    class LlmConfigFactory {
        public LlmConfig getConfig(String model) {
            switch (model.toLowerCase()) {
                case "gemini":
                    return new GeminiConfig();
                case "empathy":
                    return new EmpathyConfig();
                default:
                    return null;
            }
        }
    }

    class GeminiConfig implements LlmConfig {
        @Override
        public String getUrl(String apiKey) {
            return "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;
        }

        @Override
        public String transformRequest(String question, String answer, String model, Map<String, Boolean> modelSetupTracker) {
            List<Map<String, Object>> messages = new ArrayList<>();

            if (!modelSetupTracker.getOrDefault(model, false)) {
                messages.add(Collections.singletonMap("parts", Collections.singletonList(Collections.singletonMap("text", SETUP_MESSAGE))));
                modelSetupTracker.put(model, true);
            }

            messages.add(Collections.singletonMap("parts", Collections.singletonList(Collections.singletonMap("text", question + ";" + answer))));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", messages);

            return new org.json.JSONObject(requestBody).toString();
        }

        @Override
        public String transformResponse(String response) {
            return response; // Adjust parsing logic if needed
        }

        @Override
        public HttpHeaders getHeaders(String apiKey) {
            return new HttpHeaders();
        }
    }

    class EmpathyConfig implements LlmConfig {
        @Override
        public String getUrl(String apiKey) {
            return "https://empathyai.prod.empathy.co/v1/chat/completions";
        }

        @Override
        public String transformRequest(String question, String answer, String model, Map<String, Boolean> modelSetupTracker) {
            List<Map<String, String>> messages = new ArrayList<>();

            if (!modelSetupTracker.getOrDefault(model, false)) {
                messages.add(Map.of("role", "system", "content", SETUP_MESSAGE));
                modelSetupTracker.put(model, true);
            }

            messages.add(Map.of("role", "user", "content", question + ";" + answer));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "qwen/Qwen2.5-Coder-7B-Instruct");
            requestBody.put("messages", messages);

            return new org.json.JSONObject(requestBody).toString();
        }

        @Override
        public String transformResponse(String response) {
            return response; // Adjust parsing logic if needed
        }

        @Override
        public HttpHeaders getHeaders(String apiKey) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            return headers;
        }
    }

}
