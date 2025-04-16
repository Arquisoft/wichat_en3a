package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.Game;
import entities.Answer;
import entities.Question;
import entities.QuestionCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTests {
    @InjectMocks
    private GameService gameService;

    @Mock
    private QuestionService questionService;
    private QuestionCategory category=QuestionCategory.GEOGRAPHY;
    private int timer=20;
    private int numberOfQuestions=7;

    @Test
    public void startDefaultTest(){
        Assertions.assertNull(gameService.getGame());

        gameService.start(category);

        Game game = gameService.getGame();
        Assertions.assertNotNull(game);

        Assertions.assertEquals(10, game.getMaxNumberOfQuestions());
        Assertions.assertEquals(10, gameService.getMaxQuestions());
        Assertions.assertEquals(0, game.getPoints());
        Assertions.assertEquals(0, gameService.getPoints());
        Assertions.assertEquals(0, game.getQuestions());
        Assertions.assertEquals(0, game.getRightAnswers());
        Assertions.assertEquals(0, game.getWrongAnswers());
        Assertions.assertEquals(0, gameService.getRightAnswers());
        Assertions.assertEquals(0, gameService.getWrongAnswers());
        Assertions.assertNull(game.getCurrentQuestion());
        Assertions.assertNull(gameService.getCurrentQuestion());
        Assertions.assertEquals(category, game.getCategory());
        Assertions.assertEquals(category, gameService.getCategory());
        Assertions.assertEquals(30, game.getTimer());
        Assertions.assertEquals(30, gameService.getTimer());

        gameService.start(category);
        Assertions.assertNotNull(gameService.getGame());
        Assertions.assertNotEquals(game, gameService.getGame());
        game = gameService.getGame();
        Assertions.assertEquals(10, game.getMaxNumberOfQuestions());
        Assertions.assertEquals(10, gameService.getMaxQuestions());
        Assertions.assertEquals(0, game.getPoints());
        Assertions.assertEquals(0, gameService.getPoints());
        Assertions.assertEquals(0, game.getQuestions());
        Assertions.assertEquals(0, game.getRightAnswers());
        Assertions.assertEquals(0, game.getWrongAnswers());
        Assertions.assertEquals(0, gameService.getRightAnswers());
        Assertions.assertEquals(0, gameService.getWrongAnswers());
        Assertions.assertNull(game.getCurrentQuestion());
        Assertions.assertNull(gameService.getCurrentQuestion());
        Assertions.assertEquals(category, game.getCategory());
        Assertions.assertEquals(category, gameService.getCategory());
        Assertions.assertEquals(30, game.getTimer());
        Assertions.assertEquals(30, gameService.getTimer());
    }


    @Test
    public void startCustomizedTest(){
        Assertions.assertNull(gameService.getGame());

        gameService.start(category, timer,numberOfQuestions);

        Game game = gameService.getGame();
        Assertions.assertNotNull(game);

        Assertions.assertEquals(numberOfQuestions, game.getMaxNumberOfQuestions());
        Assertions.assertEquals(numberOfQuestions, gameService.getMaxQuestions());
        Assertions.assertEquals(0, game.getPoints());
        Assertions.assertEquals(0, gameService.getPoints());
        Assertions.assertEquals(0, game.getQuestions());
        Assertions.assertEquals(0, game.getRightAnswers());
        Assertions.assertEquals(0, game.getWrongAnswers());
        Assertions.assertEquals(0, gameService.getRightAnswers());
        Assertions.assertEquals(0, gameService.getWrongAnswers());
        Assertions.assertNull(game.getCurrentQuestion());
        Assertions.assertNull(gameService.getCurrentQuestion());
        Assertions.assertEquals(category, game.getCategory());
        Assertions.assertEquals(category, gameService.getCategory());
        Assertions.assertEquals(timer, game.getTimer());
        Assertions.assertEquals(timer, gameService.getTimer());

        gameService.start(category, timer,numberOfQuestions);
        Assertions.assertNotNull(gameService.getGame());
        Assertions.assertNotEquals(game, gameService.getGame());
        game = gameService.getGame();
        Assertions.assertEquals(numberOfQuestions, game.getMaxNumberOfQuestions());
        Assertions.assertEquals(numberOfQuestions, gameService.getMaxQuestions());
        Assertions.assertEquals(0, game.getPoints());
        Assertions.assertEquals(0, gameService.getPoints());
        Assertions.assertEquals(0, game.getQuestions());
        Assertions.assertEquals(0, game.getRightAnswers());
        Assertions.assertEquals(0, game.getWrongAnswers());
        Assertions.assertEquals(0, gameService.getRightAnswers());
        Assertions.assertEquals(0, gameService.getWrongAnswers());
        Assertions.assertNull(game.getCurrentQuestion());
        Assertions.assertNull(gameService.getCurrentQuestion());
        Assertions.assertEquals(category, game.getCategory());
        Assertions.assertEquals(category, gameService.getCategory());
        Assertions.assertEquals(timer, game.getTimer());
        Assertions.assertEquals(timer, gameService.getTimer());
    }

    @Test
    public void correctAnswerTest(){
        gameService.start(category);
        Game game = gameService.getGame();

        Assertions.assertEquals(0, gameService.getRightAnswers());
        Assertions.assertEquals(0, gameService.getPoints());
        Assertions.assertEquals(0, game.getQuestions());
        Assertions.assertEquals(0, gameService.getWrongAnswers());

        gameService.correctAnswer();
        Assertions.assertEquals(1, gameService.getRightAnswers());
        Assertions.assertEquals(100, gameService.getPoints());
        Assertions.assertEquals(1, game.getQuestions());
        Assertions.assertEquals(0, gameService.getWrongAnswers());

        gameService.correctAnswer();
        Assertions.assertEquals(2, gameService.getRightAnswers());
        Assertions.assertEquals(200, gameService.getPoints());
        Assertions.assertEquals(2, game.getQuestions());
        Assertions.assertEquals(0, gameService.getWrongAnswers());
    }

    @Test
    public void wrongAnswerTest(){
        gameService.start(category);
        Game game = gameService.getGame();

        Assertions.assertEquals(0, gameService.getRightAnswers());
        Assertions.assertEquals(0, gameService.getPoints());
        Assertions.assertEquals(0, game.getQuestions());
        Assertions.assertEquals(0, gameService.getWrongAnswers());

        gameService.wrongAnswer();
        Assertions.assertEquals(0, gameService.getRightAnswers());
        Assertions.assertEquals(-25, gameService.getPoints());
        Assertions.assertEquals(1, game.getQuestions());
        Assertions.assertEquals(1, gameService.getWrongAnswers());

        gameService.wrongAnswer();
        Assertions.assertEquals(0, gameService.getRightAnswers());
        Assertions.assertEquals(-50, gameService.getPoints());
        Assertions.assertEquals(2, game.getQuestions());
        Assertions.assertEquals(2, gameService.getWrongAnswers());
    }

    @Test
    void hasGameEndedNotEndedTest() {
        gameService.start(category);
        Assertions.assertFalse(gameService.hasGameEnded());

        // Play all but one question
        for (int i = 0; i < gameService.getMaxQuestions() - 1; i++) {
            gameService.correctAnswer();
            Assertions.assertFalse(gameService.hasGameEnded());
        }
    }

    @Test
    void hasGameEndedEndedTest() {
        gameService.start(category);
        // Play all questions
        for (int i = 0; i < gameService.getMaxQuestions(); i++) {
            gameService.correctAnswer();
        }

        Assertions.assertTrue(gameService.hasGameEnded());
    }

    @Test
    public void nextQuestionTest() {
        Question testQuestion = new Question(new Answer("Paris", "en"), "Capital of France?", "flag.jpg");
        gameService.start(category);

        // Arrange
        when(questionService.getRandomQuestion(any())).thenReturn(testQuestion);

        // Act
        gameService.nextQuestion();

        // Assert
        Assertions.assertNotNull(gameService.getGame());
        Assertions.assertEquals(testQuestion, gameService.getGame().getCurrentQuestion());
        Assertions.assertEquals(testQuestion, gameService.getCurrentQuestion());
    }

    @Test
    public void checkAnswerTest(){
        Answer answer = new Answer("Paris", "en");
        answer.setId("correct");
        Question testQuestion = new Question(answer, "Capital of France?", "flag.jpg");

        gameService.start(category);

        when(questionService.getRandomQuestion(any())).thenReturn(testQuestion);
        gameService.nextQuestion();

        gameService.checkAnswer("correct");
        Assertions.assertEquals(1, gameService.getRightAnswers());
        Assertions.assertEquals(100, gameService.getPoints());
        Assertions.assertEquals(0, gameService.getWrongAnswers());


        gameService.checkAnswer("aaa");
        Assertions.assertEquals(1, gameService.getRightAnswers());
        Assertions.assertEquals(100-25, gameService.getPoints());
        Assertions.assertEquals(1, gameService.getWrongAnswers());
    }
}
