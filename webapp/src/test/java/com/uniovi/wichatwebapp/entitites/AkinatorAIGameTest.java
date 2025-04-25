package com.uniovi.wichatwebapp.entitites;

import com.uniovi.wichatwebapp.entities.AkinatorIAGuessesGame;
import entities.QuestionCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AkinatorIAGuessesGameTest {

    private AkinatorIAGuessesGame game;
    private final QuestionCategory testCategory = QuestionCategory.GEOGRAPHY;
    private final int initialQuestionsLeft = 20;

    @BeforeEach
    void setUp() {
        game = new AkinatorIAGuessesGame(testCategory);
    }

    @Test
    void constructorTest() {
        assertEquals(testCategory, game.getCategory());
    }

    @Test
    void isAIGuessiningTest() {
        assertTrue(game.isAIGuessining());
    }

    @Test
    void getSetUpMessageChatTest() {
        // Act
        String setupMessage = game.getSetUpMessageChat();

        // Assert
        assertAll(
                () -> assertTrue(setupMessage.contains("1. You cannot ask a question that was already asked")),
                () -> assertTrue(setupMessage.contains("2. The user is thinking of something in the " + testCategory + " category")),
                () -> assertTrue(setupMessage.contains("3. You must only tell the question you want to ask")),
                () -> assertTrue(setupMessage.contains("4. The question must be a Yes or No question")),
                () -> assertTrue(setupMessage.contains("5. In the already given questions")),
                () -> assertTrue(setupMessage.contains("6. The think that the user is thinking must satisfy all the answers")),
                () -> assertTrue(setupMessage.contains("7. You have only")),
                () -> assertTrue(setupMessage.contains("8. If you are sure about the answer")),
                () -> assertTrue(setupMessage.contains("9. I recommend you to do first generic questions")),
                () -> assertTrue(setupMessage.contains("10. If you ask for a solution like a final or probable guess"))
        );
    }
}