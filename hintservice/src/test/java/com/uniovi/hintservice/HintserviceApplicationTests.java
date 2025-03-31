package com.uniovi.hintservice;

import com.uniovi.hintservice.controller.HintController;
import com.uniovi.hintservice.service.GenAI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.http.HttpException;
import static org.hamcrest.Matchers.not;

import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(HintController.class)
public class HintserviceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenAI genAI;

    @BeforeEach
    public void setup() throws IOException, HttpException {
        // Define behavior for the mocked GenAI instance
        when(genAI.askPrompt(anyString(), anyString())).thenReturn("The Eiffel tower is in this city");
    }

    @Test
    void testAnswerNotInHint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/askHint")
                .param("question", "What is the capital of France?")
                .param("answerQuestion", "Paris"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Paris"))));
    }

    @Test
    void testCorrectAnswerLength() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/askHint")
                .param("question", "What is the capital of France?")
                .param("answerQuestion", "Paris"))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().length() < 300,
                        "The response should be as short as possible"));
    }
}
