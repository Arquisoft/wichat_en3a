package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.repositories.HintRepository;
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
    @Autowired
    private HintRepository hintRepository;

    @GetMapping("/hint")
    public String getHintForm() {
        return "hint";
    }

    @GetMapping("/getHint")
    @ResponseBody
    public String getHint(@RequestParam String question, @RequestParam String answerQuestion, Model model) {
        return hintRepository.ask(question, answerQuestion);
    }

    @PostMapping("/hint")
    public String askHint(@RequestParam String question, @RequestParam String answerQuestion, Model model) {
        String result = hintRepository.ask(question, answerQuestion);
        model.addAttribute("result", result);
        return "hint";
    }

}
