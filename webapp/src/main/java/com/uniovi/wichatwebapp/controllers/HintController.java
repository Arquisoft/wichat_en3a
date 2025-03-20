package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.repositories.HintRepository;
import com.uniovi.wichatwebapp.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class HintController {
    private final static String setupMessageChat = "You are part of a web game about questions and answers. Your role is to provide clues to the player. " +
            "You will receive a message in the following format: `<question or sentence>;<answer>`. " +
            "Based on this, you must respond according to the following rules:\n\n" +

            "1. **If the question or sentence is unrelated to the answer**: " +
            "Answer the question directly and freely, as if you were a general-purpose assistant. This isn't a abnormal behaviour but DON'T give the answer passed to you" +
            "Example:\n" +
            "   - Input: 'What is the weather today?;Paris'\n" +
            "   - Response: 'I cannot provide real-time weather updates, but you can check a weather website or app.'\n\n" +

            "2. **If the question or sentence  is related to the answer**: " +
            "Provide a clue that helps the player guess the answer, but **never reveal the answer directly**. " +
            "The clue should be short, relevant, and not contain the answer. " +
            "Example:\n" +
            "   - Input: 'What is the capital of France?;Paris'\n" +
            "   - Response: 'This city is famous for the Eiffel Tower.'\n\n" +

            "3. **General guidelines**:\n" +
            "   - Keep your responses concise and to the point.\n" +
            "   - Do not include the answer in your response.\n" +
            "   - Ask the player to ask another question.\n\n" +

            "Remember: Your goal is to assist the player without giving away the answer directly.";

    @Autowired
    private HintRepository hintRepository;
    @Autowired
    private GameService game;

//    @GetMapping("/hint")
//    public String getHintForm() {
//        return "hint";
//    }

    @GetMapping("/hint")
    @ResponseBody
    public String getHint(@RequestParam String question) {
        String answer = game.getCurrentQuestion().getCorrectAnswer().getText();
        return hintRepository.askWithInstructions(setupMessageChat, question, answer);
    }

//    @PostMapping("/hint")
//    public String askHint(@RequestParam String question, @RequestParam String answerQuestion, Model model) {
//        String result = hintRepository.ask(question, answerQuestion);
//        model.addAttribute("result", result);
//        return "hint";
//    }

}
