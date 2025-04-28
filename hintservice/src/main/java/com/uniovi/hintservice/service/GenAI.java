package com.uniovi.hintservice.service;

import autovalue.shaded.com.google.common.collect.ImmutableList;
import com.google.genai.Client;
import com.google.genai.types.*;
import jakarta.annotation.PostConstruct;

import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
//@PropertySource("classpath:application.properties")
public class GenAI { //https://github.com/googleapis/java-genai
    private final static String  GEMINI_1_5 = "gemini-1.5-flash-001";
    private final static String  GEMINI_2_0 = "gemini-2.0-flash-001";
    @Value("${LLM_APIKEY:not_working}")
    private String apiKey;
    private Client client;
    private ImmutableList<SafetySetting> safetySettings;
    private Tool googleSearchTool = Tool.builder().googleSearch(GoogleSearch.builder().build()).build(); // Sets the Google Search tool in the config.


    @PostConstruct
    public void init(){
        client = Client.builder().apiKey(apiKey).build();
        this.safetySettings = initializeSafetySettings();
    }

    public String askPrompt(String instructions, String prompt) throws IOException, HttpException {
        try {
            // Sets the system instruction in the config.
            Content systemInstruction =
                    Content.builder()
                            .parts(ImmutableList.of(Part.builder().text(instructions).build()))
                            .build();

            GenerateContentConfig config =
                    GenerateContentConfig.builder()
                            .candidateCount(1)
                            .maxOutputTokens(4000)
                            .safetySettings(safetySettings)
                            .systemInstruction(systemInstruction)
                            .tools(ImmutableList.of(googleSearchTool))
                            .build();

            GenerateContentResponse response =
                    client.models.generateContent(GEMINI_2_0, prompt, config);

            return response.text();
        } catch (IOException | HttpException e) {
            throw new RuntimeException("Error calling LLM: " + e.getMessage(), e);
        }

    }

    // Sets the safety settings
    protected ImmutableList<SafetySetting> initializeSafetySettings() {
        return ImmutableList.of(
                SafetySetting.builder()
                        .category("HARM_CATEGORY_HATE_SPEECH")
                        .threshold("BLOCK_ONLY_HIGH")
                        .build(),
                SafetySetting.builder()
                        .category("HARM_CATEGORY_DANGEROUS_CONTENT")
                        .threshold("BLOCK_LOW_AND_ABOVE")
                        .build());
    }
}
