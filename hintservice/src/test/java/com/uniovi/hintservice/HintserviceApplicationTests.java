package com.uniovi.hintservice;

import com.uniovi.hintservice.controller.HintController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class HintserviceApplicationTests {

	@Autowired
	private HintController hC;

	@Test
	void testAnswerNotInHint() throws Exception{
		String question = "What is the capital of France";
		String answer = "Paris";
		String response = hC.askHint(question, answer);

		Assertions.assertFalse(response.contains("Paris"), "The response should not contain the answer to the question");

	}

	@Test
	void testCorrectAnswerLenght() throws Exception{
		String question = "What is the capital of France";
		String answer = "Paris";
		String response = hC.askHint(question, answer);

		Assertions.assertTrue(response.length()<300, "The response should be as short as possible");
	}

}
