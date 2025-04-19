package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.entities.Wordle;
import com.uniovi.wichatwebapp.services.WordleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class WordleControllerTest {

    @InjectMocks
    private WordleController wordleController;

    @Mock
    private WordleService wordleService;

    @Mock
    private Model model;

    private final String username = "test";
    private final Authentication auth = new UsernamePasswordAuthenticationToken(username, "password");

    @BeforeEach
    public void setupSecurityContext() {
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Mockito.when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }

    @Test
    public void startGameTest() {
        String result = wordleController.startGame();

        Mockito.verify(wordleService).startNewGame(username);
        Assertions.assertEquals("redirect:/wordle/game", result);
    }

    @Test
    public void getGameTest() {
        List<String> attempts = List.of("paris", "lima");

        Wordle gameMock = Mockito.mock(Wordle.class);
        Mockito.when(gameMock.getAttempts()).thenReturn(attempts);
        Mockito.when(wordleService.getGame(username)).thenReturn(gameMock);

        String result = wordleController.getGame(model);

        Mockito.verify(model).addAttribute(eq("wordle"), eq(gameMock));
        Mockito.verify(model).addAttribute(eq("attemptsSplit"), Mockito.any());
        Assertions.assertEquals("wordle/game", result);
    }

    @Test
    public void makeGuessTest() {
        String guess = "madrid";

        String result = wordleController.makeGuess(guess);

        Mockito.verify(wordleService).makeGuess(username, guess);
        Assertions.assertEquals("redirect:/wordle/game", result);
    }

    @Test
    public void resetGameTest() {
        String result = wordleController.resetGame();

        Mockito.verify(wordleService).resetGame(username);
        Assertions.assertEquals("redirect:/wordle/start", result);
    }
}
