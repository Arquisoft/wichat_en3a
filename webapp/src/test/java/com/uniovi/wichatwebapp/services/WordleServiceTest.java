package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.repositories.QuestionRepository;
import entities.Answer;
import entities.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class WordleServiceTest {

    @InjectMocks
    private WordleService wordleService;

    @Mock
    private QuestionRepository questionRepository;

    private Question test = new Question(new Answer("Paris", "en"), "Capital of France?", "flag.jpg");

    @BeforeEach
    public void setUp() {
        Mockito.when(questionRepository.getRandomQuestion(any())).thenReturn(test);
    }

    @Test
    public void startNewGameTest() {
        wordleService.startNewGame("user123");

        Assertions.assertEquals("PARIS", wordleService.getGame("user123").getTargetWord());
    }

    @Test
    public void makeGuessTest() {
        //There is no game for the user
        try {
            wordleService.makeGuess("Paris", "en");
        } catch (Exception e) {
            Assertions.assertEquals("There's no active game", e.getMessage());
        }

        //There is a game for the user
        wordleService.startNewGame("user123");
        wordleService.makeGuess("user123", "italy");

        Assertions.assertFalse(wordleService.getGame("user123").getAttempts().isEmpty());
    }


}
