package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.dto.AnswerDto;
import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Game;
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
import static org.mockito.Mockito.*;

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

        Assertions.assertEquals("personalized", view);
    }

    @Test
    void createPersonalizedGameTest() {
        QuestionCategory category = QuestionCategory.GEOGRAPHY;
        int timer = 12;
        int questionCount = 7;
        Question mockQuestion = new Question(new Answer("Madrid","en"), "Which is the capital of Spain?", "no-image");


        // When start() is called, set up the game and store it
        doAnswer(invocation -> {
            Game game = new Game(category, timer, questionCount);
            when(gameService.getGame()).thenReturn(game); // Make getGame() return this game
            gameService.nextQuestion();
            return null; // start() is void
        }).when(gameService).start(category, timer, questionCount);

        doAnswer(invocation -> {
            // This will be executed when nextQuestion() is called
            gameService.getGame().setCurrentQuestion(mockQuestion);
            return null; // since it's void
        }).when(gameService).nextQuestion();

        // Act
        String view = questionController.createPersonalizedGame(category,timer, questionCount);

        //Asserts
        Assertions.assertEquals(category, gameService.getGame().getCategory());
        Assertions.assertEquals(12, gameService.getGame().getTimer());
        Assertions.assertEquals(7, gameService.getGame().getMaxNumberOfQuestions());
        Assertions.assertEquals(0, gameService.getGame().getQuestions());
        Assertions.assertEquals(0, gameService.getGame().getRightAnswers());
        Assertions.assertEquals(0, gameService.getGame().getWrongAnswers());
        Assertions.assertEquals(0, gameService.getGame().getPoints());
        Question currentQuestion=gameService.getGame().getCurrentQuestion();
        Assertions.assertNotNull(currentQuestion);
        Assertions.assertEquals("Madrid", currentQuestion.getCorrectAnswer().getText());
        Assertions.assertEquals("en", currentQuestion.getCorrectAnswer().getLanguage());
        Assertions.assertEquals("Which is the capital of Spain?", currentQuestion.getContent());
        Assertions.assertEquals("no-image", currentQuestion.getImageUrl());

        Assertions.assertEquals("redirect:/game/question", view);
    }

    @Test
    void startGameDefaultTest() {
        QuestionCategory category = QuestionCategory.GEOGRAPHY;
        Question mockQuestion = new Question(new Answer("Madrid","en"), "Which is the capital of Spain?", "no-image");


        // When start() is called, set up the game and store it
        doAnswer(invocation -> {
            Game game = new Game(category);
            when(gameService.getGame()).thenReturn(game); // Make getGame() return this game
            gameService.nextQuestion();
            return null; // start() is void
        }).when(gameService).start(category);

        doAnswer(invocation -> {
            // This will be executed when nextQuestion() is called
            gameService.getGame().setCurrentQuestion(mockQuestion);
            return null; // since it's void
        }).when(gameService).nextQuestion();

        // Act
        String view = questionController.startGame(category);

        //Asserts
        Assertions.assertEquals(category, gameService.getGame().getCategory());
        Assertions.assertEquals(30, gameService.getGame().getTimer());
        Assertions.assertEquals(10, gameService.getGame().getMaxNumberOfQuestions());
        Assertions.assertEquals(0, gameService.getGame().getQuestions());
        Assertions.assertEquals(0, gameService.getGame().getRightAnswers());
        Assertions.assertEquals(0, gameService.getGame().getWrongAnswers());
        Assertions.assertEquals(0, gameService.getGame().getPoints());
        Question currentQuestion=gameService.getGame().getCurrentQuestion();
        Assertions.assertNotNull(currentQuestion);
        Assertions.assertEquals("Madrid", currentQuestion.getCorrectAnswer().getText());
        Assertions.assertEquals("en", currentQuestion.getCorrectAnswer().getLanguage());
        Assertions.assertEquals("Which is the capital of Spain?", currentQuestion.getContent());
        Assertions.assertEquals("no-image", currentQuestion.getImageUrl());

        Assertions.assertEquals("redirect:/game/question", view);
    }

    @Test
    void startGamePersonalizedTest() {
        QuestionCategory category = QuestionCategory.GEOGRAPHY;
        int timer = 12;
        int questionCount = 7;
        Question mockQuestion = new Question(new Answer("Madrid","en"), "Which is the capital of Spain?", "no-image");


        // When start() is called, set up the game and store it
        doAnswer(invocation -> {
            Game game = new Game(category, timer, questionCount);
            when(gameService.getGame()).thenReturn(game); // Make getGame() return this game
            gameService.nextQuestion();
            return null; // start() is void
        }).when(gameService).start(category, timer, questionCount);

        doAnswer(invocation -> {
            // This will be executed when nextQuestion() is called
            gameService.getGame().setCurrentQuestion(mockQuestion);
            return null; // since it's void
        }).when(gameService).nextQuestion();

        // Act
        String view = questionController.startGame(category,timer, questionCount);

        //Asserts
        Assertions.assertEquals(category, gameService.getGame().getCategory());
        Assertions.assertEquals(12, gameService.getGame().getTimer());
        Assertions.assertEquals(7, gameService.getGame().getMaxNumberOfQuestions());
        Assertions.assertEquals(0, gameService.getGame().getQuestions());
        Assertions.assertEquals(0, gameService.getGame().getRightAnswers());
        Assertions.assertEquals(0, gameService.getGame().getWrongAnswers());
        Assertions.assertEquals(0, gameService.getGame().getPoints());
        Question currentQuestion=gameService.getGame().getCurrentQuestion();
        Assertions.assertNotNull(currentQuestion);
        Assertions.assertEquals("Madrid", currentQuestion.getCorrectAnswer().getText());
        Assertions.assertEquals("en", currentQuestion.getCorrectAnswer().getLanguage());
        Assertions.assertEquals("Which is the capital of Spain?", currentQuestion.getContent());
        Assertions.assertEquals("no-image", currentQuestion.getImageUrl());

        Assertions.assertEquals("redirect:/game/question", view);
    }

    @Test
    void getAnswerCorrectTest() {
        String answerId ="1234";
        Answer answer =new Answer("Madrid","en");
        answer.setId(answerId);
        Question mockQuestion = new Question(answer, "Which is the capital of Spain?", "no-image");

        Game game = new Game(QuestionCategory.GEOGRAPHY);
        game.setCurrentQuestion(mockQuestion);

        when(gameService.getCurrentQuestion()).thenAnswer(invocation -> game.getCurrentQuestion());
        when(gameService.getPoints()).thenAnswer(invocation -> game.getPoints());
        when(gameService.getGame()).thenReturn(game);
        doAnswer(invocation -> {
            game.correctAnswer();
            return null;
        }).when(gameService).correctAnswer();

        doAnswer(invocation -> {
            if(gameService.getGame().checkAnswer(answerId)){
                gameService.correctAnswer();
            }else {
                gameService.wrongAnswer();
            }
            return null;
        }).when(gameService).checkAnswer(answerId);

        // Act
        AnswerDto answerDto = questionController.getAnswer(answerId);

        Assertions.assertEquals(1, gameService.getGame().getRightAnswers());
        Assertions.assertEquals(100, gameService.getGame().getPoints());
        Assertions.assertEquals(1, gameService.getGame().getQuestions());
        Assertions.assertNotNull(answerDto);
        Assertions.assertEquals(answerId, answerDto.getCorrectId());
        Assertions.assertEquals(100, answerDto.getPoints());
        Assertions.assertEquals(0, answerDto.getPrevPoints());
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
