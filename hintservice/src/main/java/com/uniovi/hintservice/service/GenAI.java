package com.uniovi.hintservice.service;

import autovalue.shaded.com.google.common.collect.ImmutableList;
import com.google.genai.Client;
import com.google.genai.types.*;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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

    /*
    public String exampleAskTextInput() throws IOException, HttpException {
        GenerateContentResponse response =
                client.models.generateContent(GEMINI_1_5, "What is your name?", null);
        return response.text();
    }

    public String exampleAskTextAndImageInput() throws IOException, HttpException {
        // Create parts from builder or `fromJson` method.
        Part textPart = Part.builder().text("describe the image").build();
        Part imagePart =
                Part.fromJson(
                        "{\"fileData\":{\"mimeType\":\"image/jpeg\",\"fileUri\":\"gs://path/to/image.jpg\"}}");

        Content content =
                Content.builder().role("user").parts(ImmutableList.of(textPart, imagePart)).build();

        GenerateContentResponse response =
                client.models.generateContent(GEMINI_2_0, content, null);

        System.out.println("Response: " + response.text());
        return response.text();
    }

    public String exampleAskWithConfig() throws IOException, HttpException {
        // Sets the safety settings in the config.
        ImmutableList<SafetySetting> safetySettings = initializeSafetySettings();

        // Sets the system instruction in the config.
        Content systemInstruction =
                Content.builder()
                        .parts(ImmutableList.of(Part.builder().text("Answer as concisely as possible").build()))
                        .build();

        // Sets the Google Search tool in the config.
        Tool googleSearchTool = Tool.builder().googleSearch(GoogleSearch.builder().build()).build();

        GenerateContentConfig config =
                GenerateContentConfig.builder()
                        .candidateCount(1)
                        .maxOutputTokens(1024)
                        .safetySettings(safetySettings)
                        .systemInstruction(systemInstruction)
                        .tools(ImmutableList.of(googleSearchTool))
                        .build();

        GenerateContentResponse response =
                client.models.generateContent(GEMINI_2_0, "Tell me the history of LLM", config);

        return response.text();
    }
*/
    // Sets the safety settings
    private ImmutableList<SafetySetting> initializeSafetySettings() {
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
