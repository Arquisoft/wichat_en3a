package com.uniovi.hintservice.controller;

import com.uniovi.hintservice.service.GenAI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	@Operation(summary = "Ask for a hint to the LLM")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Returns a hint for the question",
		content = {@Content(mediaType = "application/json",
		schema = @Schema(implementation = String.class))}),
		@ApiResponse(responseCode = "500", description = "The service is unavailable", content = @Content)
	})
	@GetMapping("/askHint")
	public String askHint(
			@Parameter(description = "Question asked by the user")
			@RequestParam String question,
			@Parameter(description = "Answer to the question shown to the user")
			@RequestParam String answerQuestion) throws HttpException, IOException {
		return genAI.askPrompt(setupMessage, question+";"+answerQuestion);
	}

	@Operation(summary = "Ask for a hint specifying the instructions to be given to the LLM")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Returns a hint adapted to the given instructions",
			content = {@Content(mediaType = "application/json",
			schema = @Schema(implementation = String.class))}),
			@ApiResponse(responseCode = "500", description = "The service is unavailable", content = @Content)
	})
	@GetMapping("/askHintWithInstructions")
	public String askHint(
			@Parameter(description = "Instructions on how to response sent to the LLM")
			@RequestParam String instructions,
			@Parameter(description = "Question asked by the user")
			@RequestParam String question,
			@Parameter(description = "Answer to the question shown to the user")
			@RequestParam String answerQuestion,
			@Parameter(description = "Hints already given to the user")
			@RequestParam String hints) throws HttpException, IOException {
		return genAI.askPrompt(instructions, question+";"+answerQuestion +";"+hints );
	}

	public String getSetupMessage() {
		return setupMessage;
	}
}
