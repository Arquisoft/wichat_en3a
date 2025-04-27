package com.uniovi.wichatwebapp.entitites;

import com.uniovi.wichatwebapp.entities.Game;
import com.uniovi.wichatwebapp.services.QuestionService;
import entities.Answer;
import entities.Question;
import entities.QuestionCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class GameTest {
    private Game game;
    private QuestionService questionService;
    private Question question, question2;
    private QuestionCategory category;

    @BeforeEach
    void setUp() {
        category = QuestionCategory.GEOGRAPHY; // Assign the category here
        game = new Game(category);
        questionService = mock(QuestionService.class);
        Answer correctAnswer = new Answer("Paris", "en");
        correctAnswer.setId("correct123");
        question = new Question(correctAnswer, "Capital of France?", "no-image");
        when(questionService.getRandomQuestion(category)).thenReturn(question);

        Answer answer2 = new Answer("Madrid", "en");
        answer2.setId("correct456");
        question2 = new Question(answer2, "Capital of Spain?", "no-image");
    }

    @Test
    void testNextQuestion() {
        game.nextQuestion(questionService);
        assertEquals(question, game.getCurrentQuestion());
    }

    @Test
    void testGetQuestionCategory() {
        assertEquals(category,game.getCategory());
    }

    @Test
    void testNextQuestion_FirstTime() {
        when(questionService.getRandomQuestion(category)).thenReturn(question);

        game.nextQuestion(questionService);

        assertEquals(question, game.getCurrentQuestion());
        verify(questionService, times(1)).getRandomQuestion(category);
    }
}


