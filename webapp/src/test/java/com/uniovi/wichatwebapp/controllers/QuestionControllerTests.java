package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.services.GameService;
import com.uniovi.wichatwebapp.services.HintService;
import com.uniovi.wichatwebapp.services.ScoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionControllerTests {
    @InjectMocks
    private QuestionController questionController;

    @Mock
    private GameService gameService;

    @Mock
    private ScoreService scoreService;

    @Mock
    private Model model;


    @Test
    void getPersonalizedTest() {
        // Arrange
        Map<String, Object> modelAttributes = new HashMap<>();
        when(model.addAttribute(eq("categories"), any())).thenAnswer(invocation -> {
            modelAttributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return model;
        });

        // Act
        String view = questionController.getPersonalizedGame(model);

        // Assert
        verify(model).addAttribute("categories", QuestionCategory.values());
        assertThat(modelAttributes).containsKey("categories");
        assertThat(modelAttributes.get("categories")).isEqualTo(QuestionCategory.values());
    }

    @Test
    void createPersonalizedGameTest() {
        fail();
    }

    @Test
    void startGameTest() {
        fail();
    }

    @Test
    void getAnswerTest() {
        fail();
    }

    @Test
    void nextQuestionTest() {
        fail();
    }

    @Test
    void getQuestionTest() {
        fail();
    }

    @Test
    void timeoutTest() {
        fail();
    }

    @Test
    void resultsTest() {
        fail();
    }
}
