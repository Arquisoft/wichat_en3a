package com.uniovi.wichatwebapp.entitites;

import com.uniovi.wichatwebapp.entities.GameAllCategories;
import com.uniovi.wichatwebapp.services.QuestionService;
import entities.Answer;
import entities.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameAllCategoriesTest {
    private GameAllCategories game;
    private QuestionService questionService;
    private Question question;

    @BeforeEach
    void setUp() {
        game = new GameAllCategories();
        questionService = mock(QuestionService.class);
        Answer correctAnswer = new Answer("Paris", "en");
        correctAnswer.setId("correct123");
        question = new Question(correctAnswer, "Capital of France?", "no-image");
        when(questionService.getRandomQuestionNoCategory()).thenReturn(question);
    }

    @Test
    void testNextQuestion() {
        game.nextQuestion(questionService);
        assertEquals(question, game.getCurrentQuestion());
        verify(questionService).removeQuestion(question);
    }
}
