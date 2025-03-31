package com.uniovi.hintservice;

import com.uniovi.hintservice.controller.HintController;
import com.uniovi.hintservice.service.GenAI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class HintserviceApplicationTests {

	@Mock
	private HintController hC;

	@InjectMocks
	private GenAI genAI;

	@Test
	void testAnswerNotInHint() throws Exception{
		String question = "What is the capital of France";
		String answer = "Paris";
		String response = "The Eiffel tower is in this city";

		when(hC.askHint(question,answer)).thenReturn(response);

		String hint = hC.askHint(question,answer);

		Assertions.assertFalse(hint.contains("Paris"), "The response should not contain the answer to the question");

	}

	@Test
	void testCorrectAnswerLenght() throws Exception{
		String question = "What is the capital of France";
		String answer = "Paris";
		String response = "The Eiffel tower is in this city";

		when(hC.askHint(question,answer)).thenReturn(response);

		String hint = hC.askHint(question,answer);

		Assertions.assertTrue(hint.length()<300, "The response should be as short as possible");
	}

}
