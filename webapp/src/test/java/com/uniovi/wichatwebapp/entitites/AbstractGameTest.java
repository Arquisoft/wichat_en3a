package com.uniovi.wichatwebapp.entitites;

import com.uniovi.wichatwebapp.entities.Game;
import entities.Answer;
import entities.Question;
import entities.QuestionCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Fail.fail;

@ExtendWith(MockitoExtension.class)
public class AbstractGameTest {
    private Game game;
    private Question question;
    private final QuestionCategory category = QuestionCategory.GEOGRAPHY;

    @BeforeEach
    void setUp() {
        game = new Game(category);
        Answer correctAnswer = new Answer("Paris", "en");
        correctAnswer.setId("correct123");
        question = new Question(correctAnswer, "Capital of France?", "no-image");
        game.setCurrentQuestion(question);
    }

    @Test
    void defaultConstructorTest() {
        Game testGame = new Game(category);

        Assertions.assertEquals(10, testGame.getMaxNumberOfQuestions());
        Assertions.assertEquals(0, testGame.getPoints());
        Assertions.assertEquals(0, testGame.getQuestions());
        Assertions.assertEquals(0, testGame.getRightAnswers());
        Assertions.assertEquals(0, testGame.getWrongAnswers());
        Assertions.assertNull(testGame.getCurrentQuestion());
        Assertions.assertEquals(category, testGame.getCategory());
        Assertions.assertEquals(30, testGame.getTimer());
    }

    @Test
    void fullConstructorTest() {
        Game testGame = new Game(category, 60, 5);

        Assertions.assertEquals(5, testGame.getMaxNumberOfQuestions());
        Assertions.assertEquals(0, testGame.getPoints());
        Assertions.assertEquals(0, testGame.getQuestions());
        Assertions.assertEquals(0, testGame.getRightAnswers());
        Assertions.assertEquals(0, testGame.getWrongAnswers());
        Assertions.assertNull(testGame.getCurrentQuestion());
        Assertions.assertEquals(category, testGame.getCategory());
        Assertions.assertEquals(60, testGame.getTimer());
    }

    @Test
    void checkAnswerCorrectAnswerTest() {
        Assertions.assertTrue(game.checkAnswer("correct123"));
    }

    @Test
    void checkAnswerWrongAnswerTest() {
        Assertions.assertFalse(game.checkAnswer("wrong456"));
    }

    @Test
    void hasGameEndedNotEndedTest() {
        Assertions.assertFalse(game.hasGameEnded());

        // Play all but one question
        for (int i = 0; i < game.getMaxNumberOfQuestions() - 1; i++) {
            game.correctAnswer();
            Assertions.assertFalse(game.hasGameEnded());
        }
    }

    @Test
    void hasGameEndedEndedTest() {
        // Play all questions
        for (int i = 0; i < game.getMaxNumberOfQuestions(); i++) {
            game.correctAnswer();
        }

        Assertions.assertTrue(game.hasGameEnded());
    }


    @Test
    void correctAnswerTest() {
        game.correctAnswer();

        Assertions.assertEquals(1, game.getRightAnswers());
        Assertions.assertEquals(100, game.getPoints());
        Assertions.assertEquals(1, game.getQuestions());
        Assertions.assertEquals(0, game.getWrongAnswers());

        game.correctAnswer();

        Assertions.assertEquals(2, game.getRightAnswers());
        Assertions.assertEquals(200, game.getPoints());
        Assertions.assertEquals(2, game.getQuestions());
        Assertions.assertEquals(0, game.getWrongAnswers());
    }

    @Test
    void wrongAnswerTest() {
        game.wrongAnswer();

        Assertions.assertEquals(1, game.getWrongAnswers());
        Assertions.assertEquals(-25, game.getPoints());
        Assertions.assertEquals(1, game.getQuestions());
        Assertions.assertEquals(0, game.getRightAnswers());

        game.wrongAnswer();
        Assertions.assertEquals(2, game.getWrongAnswers());
        Assertions.assertEquals(-50, game.getPoints());
        Assertions.assertEquals(2, game.getQuestions());
        Assertions.assertEquals(0, game.getRightAnswers());
    }

    @Test
    void setCurrentQuestionTest() {
        Answer newAnswer = new Answer("Berlin", "en");
        newAnswer.setId("newCorrect");
        Question newQuestion = new Question(newAnswer, "Capital of Germany?", "no-image");

        game.setCurrentQuestion(newQuestion);

        Assertions.assertEquals(newQuestion, game.getCurrentQuestion());
        Assertions.assertTrue(game.checkAnswer("newCorrect"));
        Assertions.assertTrue(game.hasQuestion(newQuestion));
    }

    @Test
    void playGameTest() {
        // Test a sequence of correct and wrong answers
        game.correctAnswer(); // Q1 correct
        game.wrongAnswer();   // Q2 wrong
        game.correctAnswer(); // Q3 correct

        Assertions.assertEquals(3, game.getQuestions());
        Assertions.assertEquals(2, game.getRightAnswers());
        Assertions.assertEquals(1, game.getWrongAnswers());
        Assertions.assertEquals(175, game.getPoints()); // 100 + (-25) + 100
    }

    @Test
    void hasQuestionTest(){
        Question question2 = new Question(new Answer("Berlin", "en"), "Capital of Germany?", "no-image");
        Assertions.assertFalse(game.isQuestionInGame(question2));
        game.setCurrentQuestion(question2);
        Assertions.assertTrue(game.isQuestionInGame(question2));
    }
}
