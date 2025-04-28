package com.uniovi.hintservice.controller;

import com.uniovi.hintservice.service.GenAI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class HintControllerTests {

	@InjectMocks
	private HintController hC;

	@Mock
    private GenAI genAI;


	@Test
	void testAnswerNotInHint() throws Exception{
		String question = "What is the capital of France";
		String answer = "Paris";
		String response = "The Eiffel tower is in this city";

		when(genAI.askPrompt(hC.getSetupMessage(), question+";"+answer)).thenReturn(response);

		String hint = hC.askHint(question,answer);

		Assertions.assertFalse(hint.contains("Paris"), "The response should not contain the answer to the question");

	}

	@Test
	void testCorrectAnswerLenght() throws Exception{
		String question = "What is the capital of France";
		String answer = "Paris";
		String response = "The Eiffel tower is in this city";

		when(genAI.askPrompt(hC.getSetupMessage(), question+";"+answer)).thenReturn(response);

		String hint = hC.askHint(question,answer);

		Assertions.assertTrue(hint.length()<300, "The response should be as short as possible");
	}

	@Test
	void askHintWithIntructions() throws Exception{
		String instructions="Don't tell the answer";
		String question = "What is the capital of France";
		String answer = "Paris";
		String hints="The Eiffel tower is in this city";
		String response = "The Eiffel tower is in this city";

		when(genAI.askPrompt(instructions, question+";"+answer+";"+hints)).thenReturn(response);

		String hint = hC.askHint(instructions,question,answer,hints);

		Assertions.assertFalse(hint.contains("Paris"), "The response should not contain the answer to the question");

	}

}
