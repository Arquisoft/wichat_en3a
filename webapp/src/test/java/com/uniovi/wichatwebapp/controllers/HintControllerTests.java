package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Game;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.repositories.HintRepository;
import com.uniovi.wichatwebapp.services.GameService;
import com.uniovi.wichatwebapp.services.HintService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HintControllerTests {
    @InjectMocks
    private HintController hintController;

    @Mock
    private HintService hintService;

    @Mock
    private GameService gameService;

    @Mock
    private HintRepository hintRepository;

    @Test
    void getHintTest() {
        //Mock a question of the game service
        String questionFromUser="In which continent it is?";
        String correctAnswer="Madrid";
        Question question = new Question(new Answer(correctAnswer,"en"), "Which is the capital of Spain?", "no-image");
        question.setId("1234");

        gameService.getGame().setCurrentQuestion(question);

        when(gameService.getCurrentQuestion()).thenReturn(question);

        //Mock a hint
        when(hintRepository.askWithInstructions(hintService.getSetupMessageChat(), questionFromUser, correctAnswer, "")).thenReturn("It's in europe");

        //Ask controller
        String hint = hintController.getHint(questionFromUser);

        Assertions.assertNotNull(hint);
        Assertions.assertFalse(hint.contains(correctAnswer), "The response should not contain the answer to the question");
        Assertions.assertTrue(hint.length()<500, "The response should be as short as possible");

        Assertions.assertTrue(hintService.alreadyGivenHints().contains(hint));
    }
}
