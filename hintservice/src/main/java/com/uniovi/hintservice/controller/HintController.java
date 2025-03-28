package com.uniovi.hintservice.controller;

import com.uniovi.hintservice.service.GenAI;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@RestController
public class HintController {
	private final static String setupMessage = "You are part of a web game about questions and answers. You will be in charge of giving clues " +
			"to the player. You will receive a question and an answer from now on in the following format <question>;<answer> " +
			"when you receive that message you will reply with a clue taking in to account both the question and the answer." +
			"Your replies must have the following characteristics: They must be as short as posible, they will consist of just a" +
			"clue with no additional information, the clues must not contain the answer in them. Example of expected response\n" +
			"you receive: 'What is the capital of France;Paris' \n" +
			"posible answer: 'In the capital of france you can visit the Eiffel Tower' ";

	@Autowired
	private GenAI genAI;

	@GetMapping("/askHint")
	public String askHint(@RequestParam String question, @RequestParam String answerQuestion) throws HttpException, IOException {
		return genAI.askPrompt(setupMessage, question+";"+answerQuestion);
	}

	@GetMapping("/askHintWithInstructions")
	public String askHint(@RequestParam String instructions, @RequestParam String question, @RequestParam String answerQuestion, @RequestParam String hints) throws HttpException, IOException {
		return genAI.askPrompt(instructions, question+";"+answerQuestion +";"+hints );
	}
}
