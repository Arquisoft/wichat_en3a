package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.services.AkinatorService;
import entities.QuestionCategory;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AkinatorControllerTests {
    @InjectMocks
    private AkinatorController akinatorController;

    @Mock
    private AkinatorService gameService;

    @Mock
    private Model model;

    private Map<String, Object> modelAttributes;

    private void addAttribute() {
        modelAttributes = new HashMap<>();
        when(model.addAttribute(any(String.class), any())).thenAnswer(invocation -> {
            modelAttributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return model;
        });
    }

    @Test
    void getAkinatorTest() {
        addAttribute();
        // Act
        String view = akinatorController.getAkinator(model);

        // Assert
        verify(model).addAttribute("categories", QuestionCategory.values());
        assertThat(modelAttributes).containsKey("categories");
        assertThat(modelAttributes.get("categories")).isEqualTo(QuestionCategory.values());

        Assertions.assertEquals("akinator/akinatorHome", view);
    }

    @Test
    void createGameTest() {
        QuestionCategory category = QuestionCategory.GEOGRAPHY;
        String mode = "ai";

        String view = akinatorController.createGame(category, mode);

        verify(gameService).start(category, mode);
        assertThat(view).isEqualTo("redirect:/akinator/game");
    }

    @Test
    void getAkinatorGameTest() {
        addAttribute();
        when(gameService.getAiMessage()).thenReturn("Test message");
        when(gameService.isAiGuessing()).thenReturn(true);

        String view = akinatorController.getAkinatorGame(model);

        verify(gameService).getAiMessage();
        verify(gameService).isAiGuessing();
        assertThat(modelAttributes).containsKeys("ai", "aiGuesses");
        assertThat(modelAttributes.get("ai")).isEqualTo("Test message");
        assertThat(modelAttributes.get("aiGuesses")).isEqualTo(true);
        assertThat(view).isEqualTo("akinator/game");
    }

    @Test
    void askQuestionTest() {
        addAttribute();
        when(gameService.getAiMessage()).thenReturn("Test message");
        when(gameService.isAiGuessing()).thenReturn(true);
        String question = "Is it a mammal?";

        String view = akinatorController.askQuestion(model, question);

        verify(gameService).askQuestion(question);
        verify(gameService).getAiMessage();
        verify(gameService).isAiGuessing();
        assertThat(modelAttributes).containsKeys("ai", "aiGuesses");
        assertThat(modelAttributes.get("ai")).isEqualTo("Test message");
        assertThat(modelAttributes.get("aiGuesses")).isEqualTo(true);
        assertThat(view).isEqualTo("redirect:/akinator/game");
    }

    @Test
    void endGameTest() {
        addAttribute();
        when(gameService.getAiMessage()).thenReturn("Test message");
        when(gameService.isAiGuessing()).thenReturn(true);
        String view = akinatorController.endGame(model);

        verify(gameService).endGame();
        verify(gameService).getAiMessage();
        verify(gameService).isAiGuessing();
        assertThat(view).isEqualTo("redirect:/akinator/game");
        assertThat(modelAttributes).containsKeys("ai", "aiGuesses");
        assertThat(modelAttributes.get("ai")).isEqualTo("Test message");
        assertThat(modelAttributes.get("aiGuesses")).isEqualTo(true);
    }

    @Test
    void answeredYesTest() {
        addAttribute();
        when(gameService.getAiMessage()).thenReturn("Test message");
        when(gameService.isAiGuessing()).thenReturn(true);
        String view = akinatorController.answeredYes(model);

        verify(gameService).answer("Yes");
        verify(gameService).getAiMessage();
        verify(gameService).isAiGuessing();
        assertThat(view).isEqualTo("redirect:/akinator/game");
        assertThat(modelAttributes).containsKeys("ai", "aiGuesses");
        assertThat(modelAttributes.get("ai")).isEqualTo("Test message");
        assertThat(modelAttributes.get("aiGuesses")).isEqualTo(true);
    }

    @Test
    void answeredProbablyYesTest() {
        addAttribute();
        when(gameService.getAiMessage()).thenReturn("Test message");
        when(gameService.isAiGuessing()).thenReturn(true);
        String view = akinatorController.answeredProbablyYes(model);

        verify(gameService).answer("Probably yes");
        verify(gameService).getAiMessage();
        verify(gameService).isAiGuessing();
        assertThat(view).isEqualTo("redirect:/akinator/game");
        assertThat(modelAttributes).containsKeys("ai", "aiGuesses");
        assertThat(modelAttributes.get("ai")).isEqualTo("Test message");
        assertThat(modelAttributes.get("aiGuesses")).isEqualTo(true);
    }

    @Test
    void answeredDontKnowTest() {
        addAttribute();
        when(gameService.getAiMessage()).thenReturn("Test message");
        when(gameService.isAiGuessing()).thenReturn(true);
        String view = akinatorController.answeredDontKnow(model);

        verify(gameService).answer("Don't know");
        verify(gameService).getAiMessage();
        verify(gameService).isAiGuessing();
        assertThat(view).isEqualTo("redirect:/akinator/game");
        assertThat(modelAttributes).containsKeys("ai", "aiGuesses");
        assertThat(modelAttributes.get("ai")).isEqualTo("Test message");
        assertThat(modelAttributes.get("aiGuesses")).isEqualTo(true);
    }

    @Test
    void answeredProbablyNoTest() {
        addAttribute();
        when(gameService.getAiMessage()).thenReturn("Test message");
        when(gameService.isAiGuessing()).thenReturn(true);
        String view = akinatorController.answeredProbablyNo(model);

        verify(gameService).answer("Probably no");
        verify(gameService).getAiMessage();
        verify(gameService).isAiGuessing();
        assertThat(view).isEqualTo("redirect:/akinator/game");
        assertThat(modelAttributes).containsKeys("ai", "aiGuesses");
        assertThat(modelAttributes.get("ai")).isEqualTo("Test message");
        assertThat(modelAttributes.get("aiGuesses")).isEqualTo(true);
    }

    @Test
    void answeredNoTest() {
        addAttribute();
        when(gameService.getAiMessage()).thenReturn("Test message");
        when(gameService.isAiGuessing()).thenReturn(true);
        String view = akinatorController.answeredNo(model);

        verify(gameService).answer("No");
        verify(gameService).getAiMessage();
        verify(gameService).isAiGuessing();
        assertThat(view).isEqualTo("redirect:/akinator/game");
        assertThat(modelAttributes).containsKeys("ai", "aiGuesses");
        assertThat(modelAttributes.get("ai")).isEqualTo("Test message");
        assertThat(modelAttributes.get("aiGuesses")).isEqualTo(true);
    }
}
