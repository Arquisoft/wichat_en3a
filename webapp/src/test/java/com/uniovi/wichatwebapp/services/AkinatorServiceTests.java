package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.AkinatorGame;
import com.uniovi.wichatwebapp.entities.AkinatorIAGuessesGame;
import com.uniovi.wichatwebapp.entities.AkinatorPlayerGuessesGame;
import com.uniovi.wichatwebapp.repositories.HintRepository;
import entities.Question;
import entities.QuestionCategory;
import entities.Answer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AkinatorServiceTests {
    private Question testQuestion;
    private final QuestionCategory testCategory = QuestionCategory.GEOGRAPHY;


    @Mock
    private HintRepository hintRepository;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private AkinatorService akinatorService;


    @BeforeEach
    void setUp() {
        testQuestion = new Question();
        testQuestion.setCorrectAnswer(new Answer("Yes","en"));
    }

    @Test
    void startAIGuessesGameTest() {
        // Arrange
        String aiMode = "ai";
        when(hintRepository.askWithInstructions(anyString(), anyString(), anyString(), anyString()))
                .thenReturn("Is it a mammal?");

        // Act
        akinatorService.start(testCategory, aiMode);

        // Assert
        assertTrue(akinatorService.isAiGuessing());
        assertNotNull(akinatorService.getAiMessage());
        verify(hintRepository).askWithInstructions(anyString(), eq(""), eq(""), eq(""));
    }

    @Test
    void startPlayerGameTests() {
        // Arrange
        String playerMode = "normal";
        when(questionService.getRandomQuestion(testCategory)).thenReturn(testQuestion);

        // Act
        akinatorService.start(testCategory, playerMode);

        // Assert
        assertFalse(akinatorService.isAiGuessing());
        assertEquals("Make a question", akinatorService.getAiMessage());
        verify(questionService, atLeastOnce()).getRandomQuestion(testCategory);
    }

    @Test
    void startPlayerGameWithNumericAnswerTests() {
        // Arrange
        String playerMode = "normal";
        Question numericQuestion = new Question();
        numericQuestion.setCorrectAnswer(new Answer("123", "en"));

        when(questionService.getRandomQuestion(testCategory))
                .thenReturn(numericQuestion)
                .thenReturn(testQuestion);

        // Act
        akinatorService.start(testCategory, playerMode);

        // Assert
        verify(questionService, times(2)).getRandomQuestion(testCategory);
        assertFalse(akinatorService.isAiGuessing());
    }

    @Test
    void isNumericTest() {
        assertTrue(akinatorService.isNumeric("123"));
        assertTrue(akinatorService.isNumeric("123.45"));
        assertTrue(akinatorService.isNumeric("-123"));

        assertFalse(akinatorService.isNumeric("abc"));
        assertFalse(akinatorService.isNumeric("123abc"));
        assertFalse(akinatorService.isNumeric(""));
    }

    @Test
    void askToAiQuestionTest() {
        // Arrange
        String expectedQuestion = "Is it a mammal?";
        String initialQuestion = "Initial question?";

        // First call returns initial question, second returns our test question
        when(hintRepository.askWithInstructions(anyString(), eq(""), eq(""), anyString()))
                .thenReturn(initialQuestion)
                .thenReturn(expectedQuestion);

        akinatorService.start(testCategory, "ai");

        // Act - this will be the second call
        String result = akinatorService.askToAiQuestion();

        // Assert
        assertEquals(expectedQuestion, result);
        verify(hintRepository, times(2)).askWithInstructions(anyString(), eq(""), eq(""), anyString());
    }

    @Test
    void askToAiAnswerTest() {
        // Arrange
        String playerQuestion = "Is it a mammal?";
        String expectedAnswer = "Yes";
        when(hintRepository.askWithInstructions(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(expectedAnswer);
        when(questionService.getRandomQuestion(testCategory)).thenReturn(testQuestion);
        akinatorService.start(testCategory, "normal");

        // Act
        String result = akinatorService.askToAiAnswer(playerQuestion);

        // Assert
        assertEquals(expectedAnswer, result);
        verify(hintRepository).askWithInstructions(anyString(), eq(playerQuestion), eq(""), anyString());
    }

    @Test
    void askQuestionTest() {
        // Arrange
        String playerQuestion = "Is it a mammal?";
        String aiResponse = "Yes";
        when(hintRepository.askWithInstructions(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(aiResponse);
        when(questionService.getRandomQuestion(testCategory)).thenReturn(testQuestion);

        akinatorService.start(testCategory, "normal");

        // Act
        akinatorService.askQuestion(playerQuestion);

        // Assert
        assertEquals(aiResponse, akinatorService.getAiMessage());
        verify(hintRepository).askWithInstructions(anyString(), eq(playerQuestion), eq(""), anyString());
    }

    @Test
    void answerTest() {
        // Arrange
        String playerAnswer = "Yes";
        String nextQuestion = "Does it live in water?";
        when(hintRepository.askWithInstructions(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(nextQuestion);
        akinatorService.start(testCategory, "ai");

        // Act
        akinatorService.answer(playerAnswer);

        // Assert
        assertEquals(nextQuestion, akinatorService.getAiMessage());
        verify(hintRepository).askWithInstructions(anyString(), eq(""), eq(""), contains(playerAnswer));
    }

    @Test
    void endGameTest() {
        // Arrange
        akinatorService.start(testCategory, "ai");

        // Act
        akinatorService.endGame();
    }
}