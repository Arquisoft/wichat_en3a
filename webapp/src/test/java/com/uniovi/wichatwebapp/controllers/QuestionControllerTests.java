package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.dto.AnswerDto;
import com.uniovi.wichatwebapp.entities.*;
import com.uniovi.wichatwebapp.services.GameService;
import com.uniovi.wichatwebapp.services.HintService;
import com.uniovi.wichatwebapp.services.ScoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        Assertions.assertEquals(0, gameService.getGame().getWrongAnswers());
        Assertions.assertEquals(1, gameService.getGame().getRightAnswers());
        Assertions.assertEquals(100, gameService.getGame().getPoints());
        Assertions.assertEquals(1, gameService.getGame().getQuestions());
        Assertions.assertNotNull(answerDto);
        Assertions.assertEquals(answerId, answerDto.getCorrectId());
        Assertions.assertEquals(100, answerDto.getPoints());
        Assertions.assertEquals(0, answerDto.getPrevPoints());
    }

    @Test
    void getAnswerWrongTest() {
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
            game.wrongAnswer();
            return null;
        }).when(gameService).wrongAnswer();

        doAnswer(invocation -> {
            if(gameService.getGame().checkAnswer("jhygtrf")){
                gameService.correctAnswer();
            }else {
                gameService.wrongAnswer();
            }
            return null;
        }).when(gameService).checkAnswer("jhygtrf");

        // Act
        AnswerDto answerDto = questionController.getAnswer("jhygtrf");

        Assertions.assertEquals(1, gameService.getGame().getWrongAnswers());
        Assertions.assertEquals(0, gameService.getGame().getRightAnswers());
        Assertions.assertEquals(-25, gameService.getGame().getPoints());
        Assertions.assertEquals(1, gameService.getGame().getQuestions());
        Assertions.assertNotNull(answerDto);
        Assertions.assertEquals(answerId, answerDto.getCorrectId());
        Assertions.assertEquals(-25, answerDto.getPoints());
        Assertions.assertEquals(0, answerDto.getPrevPoints());
    }

    @Test
    void nextQuestionNotEndedGameTest() {
        Game game = new Game(QuestionCategory.GEOGRAPHY);

        when(gameService.hasGameEnded()).thenAnswer(invocation -> game.hasGameEnded());

        // Act
        String view = questionController.nextQuestion();

        Assertions.assertEquals("redirect:/game/question", view);
    }

    @Test
    void nextQuestionEndedGameTest() {
        Game game = new Game(QuestionCategory.GEOGRAPHY);

        for(int i=0; i<game.getMaxNumberOfQuestions(); i++){
            game.correctAnswer();
        }

        when(gameService.hasGameEnded()).thenAnswer(invocation -> game.hasGameEnded());

        // Act
        String view = questionController.nextQuestion();

        Assertions.assertEquals("redirect:/game/results", view);
    }

    @Test
    void getQuestionTest() {
        // Arrange
        Map<String, Object> modelAttributes = new HashMap<>();

        // Mock model behavior to capture attributes
        when(model.addAttribute(anyString(), any())).thenAnswer(invocation -> {
            modelAttributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return model;
        });

        // Mock game service responses
        Question mockQuestion = new Question(new Answer("Madrid", "en"), "Capital of Spain?", "no-image");
        when(gameService.getCurrentQuestion()).thenReturn(mockQuestion);
        when(gameService.getPoints()).thenReturn(100);
        when(gameService.getTimer()).thenReturn(30);

        // Act
        String viewName = questionController.getQuestion(model);

        // Assert
        // Verify model attributes were added correctly
        verify(model).addAttribute("question", mockQuestion);
        verify(model).addAttribute("points", 100);
        verify(model).addAttribute("timer", 30);

        // Verify attributes in the captured map
        assertThat(modelAttributes)
                .containsOnlyKeys("question", "points", "timer")
                .containsEntry("question", mockQuestion)
                .containsEntry("points", 100)
                .containsEntry("timer", 30);

        // Verify view name
        Assertions.assertEquals("question/question", viewName);
    }

    @Test
    void timeoutTest() {
        // Arrange
        Game game = new Game(QuestionCategory.GEOGRAPHY);

        // Mock wrongAnswer() to track calls and update game state
        doAnswer(invocation -> {
            game.wrongAnswer();
            return null;
        }).when(gameService).wrongAnswer();

        // Act
        String redirect = questionController.timeout();

        // Assert
        // Verify service interactions
        verify(gameService).wrongAnswer();

        // Verify game state was updated
        Assertions.assertEquals(1, game.getWrongAnswers());
        Assertions.assertEquals(-25, game.getPoints());

        // Verify redirect
        Assertions.assertEquals("redirect:/game/next", redirect);
    }

    @Test
    void resultsEndedGameTest() {
        // Arrange
        Map<String, Object> modelAttributes = new HashMap<>();
        QuestionCategory category = QuestionCategory.GEOGRAPHY;
        String username = "testUser";

        // Mock authentication
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Mock model behavior
        when(model.addAttribute(anyString(), any())).thenAnswer(invocation -> {
            modelAttributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return model;
        });

        // Mock game service responses
        when(gameService.hasGameEnded()).thenReturn(true);
        when(gameService.getCategory()).thenReturn(category);
        when(gameService.getPoints()).thenReturn(100);
        when(gameService.getRightAnswers()).thenReturn(5);
        when(gameService.getWrongAnswers()).thenReturn(2);
        when(gameService.getTimer()).thenReturn(30);
        when(gameService.getMaxQuestions()).thenReturn(10);

        final Score[] score = {null};
        // Mock score service
        doAnswer(invocation -> {
            score[0] = invocation.getArgument(0); // Capturamos el primer argumento
            return null; // asumiendo que addScore() es void
        }).when(scoreService).addScore(any(Score.class));

        // Act
        String viewName = questionController.results(model);

        // Verify model attributes
        Assertions.assertTrue(gameService.hasGameEnded());
        Assertions.assertEquals(category.toString(), score[0].getCategory());
        Assertions.assertEquals(username, score[0].getUser());
        Assertions.assertEquals(100, score[0].getScore());
        Assertions.assertEquals(5, score[0].getRightAnswers());
        Assertions.assertEquals(2, score[0].getWrongAnswers());
        Assertions.assertEquals(username, modelAttributes.get("player"));
        Assertions.assertEquals(100, modelAttributes.get("points"));
        Assertions.assertEquals(5, modelAttributes.get("right"));
        Assertions.assertEquals(2, modelAttributes.get("wrong"));
        Assertions.assertEquals(category.name(), modelAttributes.get("category"));
        Assertions.assertEquals(30, modelAttributes.get("timer"));
        Assertions.assertEquals(10, modelAttributes.get("questions"));

        verify(scoreService).addScore(any(Score.class));
        verify(gameService).start(category);
        Assertions.assertEquals("question/results", viewName);
    }

    @Test
    void resultsTestGameNotEnded() {
        // Arrange
        QuestionCategory category = QuestionCategory.GEOGRAPHY;
        when(gameService.hasGameEnded()).thenReturn(false);
        when(gameService.getCategory()).thenReturn(category);

        // Act
        String view = questionController.results(model);

        // Assert
        Assertions.assertEquals("redirect:/game/start/GEOGRAPHY", view);
        verifyNoInteractions(scoreService);
    }
}
