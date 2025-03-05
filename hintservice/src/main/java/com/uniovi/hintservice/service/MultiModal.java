package com.uniovi.hintservice.service;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Blob;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.api.HarmCategory;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.api.SafetySetting;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseStream;
import com.google.protobuf.ByteString;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiModal {
    public static String ask() throws IOException {
        try (VertexAI vertexAi = new VertexAI("gen-lang-client-0032332899", "europe-southwest1"); ) {
            GenerationConfig generationConfig =
                    GenerationConfig.newBuilder()
                            .setMaxOutputTokens(8192)
                            .setTemperature(1F)
                            .setTopP(0.95F)
                            .build();
            List<SafetySetting> safetySettings = Arrays.asList(
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_HATE_SPEECH)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_LOW_AND_ABOVE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_LOW_AND_ABOVE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_LOW_AND_ABOVE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_HARASSMENT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_LOW_AND_ABOVE)
                            .build()
            );
            var image1 = PartMaker.fromMimeTypeAndData(
                    "image/jpeg", "https://www.paises.net/wp-content/uploads/2022/11/francia-pais.jpg");
            var siText1 = "You are part of a web game about questions and answers. You will be in charge of giving clues to the player. You will receive a question and an answer from now on in the following format <question>;<answer> when you receive that message you will reply with a clue taking into account both the question and the answer. Your replies must be as short as possible, they will consist of just a clue with no additional information, the clues must not contain the answer in them.";

            var content = ContentMaker.fromMultiModalData("<¿Cuál es la capital de Francia?>;<París>", image1);

            var systemInstruction = ContentMaker.fromMultiModalData(siText1);
            GenerativeModel model =
                    new GenerativeModel.Builder()
                            .setModelName("gemini-1.5-flash-002")
                            .setVertexAi(vertexAi)
                            .setGenerationConfig(generationConfig)
                            .setSafetySettings(safetySettings)
                            .setSystemInstruction(systemInstruction)
                            .build();


            ResponseStream<GenerateContentResponse> responseStream = model.generateContentStream(content);

            StringBuilder responseString = new StringBuilder();
            responseStream.stream().forEach(response -> {
                // Extraer el texto de cada respuesta
                String generatedText = response.toString(); // Aquí se supone que 'getContent()' obtiene el texto generado
                responseString.append(generatedText).append("\n");
            });

            // Convertir la respuesta a un String
            String finalResponse = responseString.toString();
            return finalResponse;
        }
    }
}
