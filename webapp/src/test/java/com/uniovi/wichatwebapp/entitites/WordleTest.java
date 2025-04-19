package com.uniovi.wichatwebapp.entitites;

import com.uniovi.wichatwebapp.entities.Wordle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class WordleTest {

    private Wordle mock;

    @BeforeEach
    public void setup() {
        mock = new Wordle("italy");
    }

    @Test
    public void constructorTest() {
        Assertions.assertEquals("italy", mock.getTargetWord().toLowerCase());
        Assertions.assertEquals(5, mock.getMaxAttempts());
        Assertions.assertTrue(mock.getAttempts().isEmpty());
        Assertions.assertEquals(Wordle.GameStatus.PLAYING, mock.getStatus());
        Assertions.assertTrue(mock.getFeedbackHistory().isEmpty());
    }

    @Test
    public void settersTest() {
        String newTargetWord = "greece";
        List<String> newAttemps = List.of("france","canada");
        int newMaxAttempts = 6;
        Wordle.GameStatus newStatus = Wordle.GameStatus.LOSE;

        mock.setTargetWord(newTargetWord);
        mock.setAttempts(newAttemps);
        mock.setMaxAttempts(newMaxAttempts);
        mock.setStatus(newStatus);

        Assertions.assertEquals("greece", mock.getTargetWord().toLowerCase());
        Assertions.assertEquals(6, mock.getMaxAttempts());
        Assertions.assertEquals(newAttemps, mock.getAttempts());
        Assertions.assertEquals(Wordle.GameStatus.LOSE, mock.getStatus());
    }

    @Test
    public void guessTest() {
        mock.guess("spain");

        Assertions.assertFalse(mock.getAttempts().isEmpty());
        Assertions.assertFalse(mock.getFeedbackHistory().isEmpty());
        Assertions.assertEquals(Wordle.GameStatus.PLAYING, mock.getStatus());
    }
}
