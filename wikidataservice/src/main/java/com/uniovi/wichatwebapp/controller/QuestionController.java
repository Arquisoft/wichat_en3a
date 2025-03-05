package com.uniovi.wichatwebapp.controller;

import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping(
            value = {"/question/{id}"},
            method = {RequestMethod.GET}
    )
    public String getQuestion(@RequestParam String id, Model model) {
        Question question = questionService.findQuestionById(id);
        model.addAttribute("question", question);
        return "question";
    }
    @RequestMapping(
            value = {"/game/chooseAnswer/{id}"},
            method = {RequestMethod.GET}
    )
    public String chooseAnswer(@RequestParam String id,Model model) {
        questionService.checkAnswer(id);
        return "question";
    }
}
