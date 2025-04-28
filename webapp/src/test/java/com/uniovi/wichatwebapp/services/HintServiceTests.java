package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.repositories.HintRepository;
import entities.Answer;
import entities.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HintServiceTests {
    @InjectMocks
    private HintService hintService;

    @Mock
    private HintRepository hintRepository;

    @Test
    void getHintTest() {
        //Mock a question of the game service
        String questionFromUser="In which continent it is?";
        String correctAnswer="Madrid";
        Question question = new Question(new Answer(correctAnswer,"en"), "Which is the capital of Spain?", "no-image");
        question.setId("1234");

        //Mock a hint
        when(hintRepository.askWithInstructions(hintService.getSetupMessageChat(), questionFromUser, correctAnswer, "")).thenReturn("It's in europe");

        //Ask controller
        String hint = hintService.askQuestionToIA(question, questionFromUser);

        Assertions.assertNotNull(hint);
        Assertions.assertFalse(hint.contains(correctAnswer), "The response should not contain the answer to the question");
        Assertions.assertTrue(hint.length()<500, "The response should be as short as possible");

        Assertions.assertTrue(hintService.alreadyGivenHints().contains(hint));

        Question question2 = new Question(new Answer(correctAnswer,"en"), "Which is the capital of Spain?", "no-image");
        question2.setId("567");
        hint = hintService.askQuestionToIA(question2, questionFromUser);
        Assertions.assertNotNull(hint);
        Assertions.assertFalse(hint.contains(correctAnswer), "The response should not contain the answer to the question");
        Assertions.assertTrue(hint.length()<500, "The response should be as short as possible");

        Assertions.assertTrue(hintService.alreadyGivenHints().contains(hint));
    }
}
