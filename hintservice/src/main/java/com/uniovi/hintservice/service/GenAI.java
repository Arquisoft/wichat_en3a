package com.uniovi.hintservice.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
//@PropertySource("classpath:application.properties")
public class GenAI { //https://github.com/googleapis/java-genai
    private String apiKey;

    @Autowired
    public GenAI(Environment environment) {
//        this.apiKey = environment.getProperty("llm.api.key");
//        System.out.println("API Key: " + apiKey); // Verifica que la propiedad no sea null
        // Cargar el archivo .env
        Dotenv dotenv = Dotenv.load();

        // Obtener la clave API del archivo .env
        this.apiKey = dotenv.get("llm.apikey");
        System.out.println("API Key: " + apiKey);
    }

    public String ask() throws IOException, HttpException {
        Client client = Client.builder().apiKey(apiKey).build();

        GenerateContentResponse response =
                client.models.generateContent("gemini-1.5-flash-001", "What is your name?", null);

        // Gets the text string from the response by the quick accessor method `text()`.
        return response.text();
    }
}
