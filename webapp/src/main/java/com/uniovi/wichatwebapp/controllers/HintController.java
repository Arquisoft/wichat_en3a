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
    private final static String setupMessageChat = "You are part of a web game about questions and answers. You will be in charge of giving clues " +
            "to the player. You will receive a question from the user and an answer of the question that he is seeing from now on in the following format <question>;<answer> " +
            "when you receive that message you will reply with a clue taking in to account both the question and the answer." +
            "Your replies must have the following characteristics: They must be as short as posible, they will consist of just a" +
            "clue with no additional information, the clues must not contain the answer in them. Example of expected response\n" +
            "you receive: 'What is the capital of France;Paris' \n" +
            "posible answer: 'In the capital of france you can visit the Eiffel Tower' ";

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
