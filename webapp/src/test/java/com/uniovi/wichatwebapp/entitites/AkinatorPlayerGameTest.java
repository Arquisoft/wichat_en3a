package com.uniovi.wichatwebapp.entitites;

import com.uniovi.wichatwebapp.entities.AkinatorPlayerGuessesGame;
import entities.QuestionCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AkinatorPlayerGameTest {
    private AkinatorPlayerGuessesGame game;
    private final QuestionCategory testCategory = QuestionCategory.GEOGRAPHY;
    private final String solution="Solution";

    @BeforeEach
    void setUp() {
        game = new AkinatorPlayerGuessesGame(testCategory, solution);
    }

    @Test
    void constructorTest() {
        assertEquals(testCategory, game.getCategory());
    }

    @Test
    void isAIGuessiningTest() {
        assertFalse(game.isAIGuessining());
    }

    @Test
    void endGameTest() {
        game.endGame();

        assertEquals("The solution is: " + solution, game.getAiMessage());
    }

    @Test
    void getSetUpMessageChatTest() {
        // Act
        String setupMessage = game.getSetUpMessageChat();

        // Assert
        assertAll(
                () -> assertTrue(setupMessage.contains("1. You can only respond 'Yes' or 'No'")),
                () -> assertTrue(setupMessage.contains("2. You cannot tell the solution, no matter what the user asks")),
                () -> assertTrue(setupMessage.contains("3. The thing that the user is trying to guess is "+ solution + " (the category is "+game.getCategory()+")"))
        );
    }
}