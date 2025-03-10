package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.services.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping(
                value = {"/game/question"},
            method = {RequestMethod.GET}
    )
    public String getQuestion(Model model) {
        Question question = questionService.getRandomQuestion();
        model.addAttribute("question", question);
        return "question/question";
    }

    @RequestMapping(
            value = {"/game/chooseAnswer/{id}"},
            method = {RequestMethod.GET}
    )
    public String chooseAnswer(@PathVariable String id, Model model) {
        Question question = (Question)model.getAttribute("question");
        if(questionService.checkAnswer(id, question)){
            questionService.removeQuestion(question);
            return "redirect:/game/question/";
        }
        return "redirect:/game/wrongAnswer";
    }

    @RequestMapping(
            value = {"/game/wrongAnswer"},
            method = {RequestMethod.GET}
    )
    public String wrongAnswer() {

        return "question/wrongAnswer";
    }


}
