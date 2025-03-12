package com.uniovi.wichatwebapp.controller;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.service.QuestionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping(
            value = {"/game/newQuestion"},
            method = {RequestMethod.GET}
    )
    public Question getQuestion() {
        return questionService.getRandomQuestion("en");
    }

    @RequestMapping(
            value = {"/game/getCorrectAnswer/{id}"},
            method = {RequestMethod.GET}
    )
    public Answer getCorrectAnswer(@PathVariable String id) {
        Question question = questionService.findQuestionById(id);
        return questionService.findAnswerById(question.getCorrectAnswerId());
    }

    @RequestMapping(
            value = {"/game/getAnswer/{id}"},
            method = {RequestMethod.GET}
    )
    public Answer getAnswer(@PathVariable String  id) {
        return questionService.findAnswerById(id);
    }

    @RequestMapping(
            value = {"/game/removeQuestion/{id}"},
            method = {RequestMethod.GET}
    )
    public void removeQuestion(String id){
        questionService.removeQuestion(questionService.findQuestionById(id));
    }
}
