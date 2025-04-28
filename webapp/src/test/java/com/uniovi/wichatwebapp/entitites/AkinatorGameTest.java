package com.uniovi.wichatwebapp.entitites;

import com.uniovi.wichatwebapp.entities.AkinatorGame;
import entities.QuestionCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AkinatorGameTest {

    // Test concrete implementation of AkinatorGame
    private static class TestAkinatorGame extends AkinatorGame {
        private final boolean isAiGuessing;

        public TestAkinatorGame(QuestionCategory category, boolean isAiGuessing) {
            super(category);
            this.isAiGuessing = isAiGuessing;
        }

        @Override
        public boolean isAIGuessining() {
            return isAiGuessing;
        }

        @Override
        public String getSetUpMessageChat() {
            return "Test setup message";
        }
    }

    private AkinatorGame game;
    private final QuestionCategory testCategory = QuestionCategory.GEOGRAPHY;

    @BeforeEach
    void setUp() {
        game = new TestAkinatorGame(testCategory, false);
    }

    @Test
    void getCategoryTest() {
        assertEquals(testCategory, game.getCategory());
    }

    @Test
    void addQuestionsAndAnswersTest() {
        assertEquals("", game.getAlreadyQuestionsAndAnswers());

        // Arrange
        String question1 = "Is it a mammal?";
        String answer1 = "Yes";
        String question2 = "Does it live in water?";
        String answer2 = "No";

        // Act
        game.addQuestionsAndAnswers(question1, answer1);
        game.addQuestionsAndAnswers(question2, answer2);
        String result = game.getAlreadyQuestionsAndAnswers();

        // Assert
        assertTrue(result.contains(question1 + "_" + answer1 + "_"));
        assertTrue(result.contains(question2 + "_" + answer2 + "_"));
    }

    @Test
    void isAIGuessiningTest() {
        // Test with AI guessing
        AkinatorGame aiGame = new TestAkinatorGame(testCategory, true);
        assertTrue(aiGame.isAIGuessining());

        // Test with player guessing
        AkinatorGame playerGame = new TestAkinatorGame(testCategory, false);
        assertFalse(playerGame.isAIGuessining());
    }

    @Test
    void aiMessageTest() {
        // Arrange
        String message = "Test message";

        // Act
        game.setAiMessage(message);

        // Assert
        assertEquals(message, game.getAiMessage());
    }

    @Test
    void getSetUpMessageChatTest() {
        assertEquals("Test setup message", game.getSetUpMessageChat());
    }
}