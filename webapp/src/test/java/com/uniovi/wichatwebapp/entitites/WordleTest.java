package com.uniovi.wichatwebapp.entitites;

import com.uniovi.wichatwebapp.entities.Wordle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class WordleTest {

    private Wordle mock;

    @BeforeEach
    public void setup() {
        mock = new Wordle("italy");
    }

    @Test
    public void constructorTest() {
        assertEquals("italy", mock.getTargetWord().toLowerCase());
        assertEquals(5, mock.getMaxAttempts());
        Assertions.assertTrue(mock.getAttempts().isEmpty());
        assertEquals(Wordle.GameStatus.PLAYING, mock.getStatus());
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

        assertEquals("greece", mock.getTargetWord().toLowerCase());
        assertEquals(6, mock.getMaxAttempts());
        assertEquals(newAttemps, mock.getAttempts());
        assertEquals(Wordle.GameStatus.LOSE, mock.getStatus());
    }

    @Test
    public void guessTest() {
        //Guess in a newly started game
        mock.guess("spain");

        Assertions.assertFalse(mock.getAttempts().isEmpty());
        Assertions.assertFalse(mock.getFeedbackHistory().isEmpty());
        assertEquals(Wordle.GameStatus.PLAYING, mock.getStatus());

        //Guessing the correct word
        mock.guess("italy");

        assertEquals(Wordle.GameStatus.WIN, mock.getStatus());

        //Guessing in a game with a status different from PLAYING
        mock.setStatus(Wordle.GameStatus.LOSE);
        int remaining = mock.getRemainingAttempts();
        mock.guess("paris");

        assertEquals(remaining, mock.getRemainingAttempts());

        //Last guess of a game is wrong
        mock.setStatus(Wordle.GameStatus.PLAYING);
        mock.getAttempts().add("PARIS");
        mock.getAttempts().add("PARIS");

        mock.guess("Light");

        assertEquals(Wordle.GameStatus.LOSE, mock.getStatus());

        mock.setStatus(Wordle.GameStatus.LOSE);
        for(int i=0; i<=11; i++) {
            mock.getAttempts().add("PARIS");
        }
        mock.guess("Light");
    }
}
