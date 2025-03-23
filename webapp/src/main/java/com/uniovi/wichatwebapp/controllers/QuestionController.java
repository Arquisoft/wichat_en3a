package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.services.GameService;
import com.uniovi.wichatwebapp.services.QuestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Random;

@Controller
public class QuestionController {
    private final QuestionService questionService;
    private final GameService gameService;

    public QuestionController(QuestionService questionService, GameService gameService) {
        this.questionService = questionService;
        this.gameService = gameService;
    }

    @RequestMapping(
            value = {"/game/question"},
            method = {RequestMethod.GET}
    )
    public String getQuestion(Model model, HttpSession session) {
        model.addAttribute("points", gameService.getPoints());
        QuestionCategory category = (QuestionCategory)session.getAttribute("category");
        if(category == null){
            category = QuestionCategory.values()[new Random().nextInt(QuestionCategory.values().length)];
        }
        return getQuestion(category, model, session);
    }

    @RequestMapping(
                value = {"/game/question/{category}"},
            method = {RequestMethod.GET}
    )
    public String getQuestion(@PathVariable QuestionCategory category, Model model, HttpSession session) {
        model.addAttribute("points", gameService.getPoints());
        Question question = questionService.getRandomQuestion(category);
        model.addAttribute("question", question);
        if(session.getAttribute("category") == null){
            session.setAttribute("category", category);
        }
        session.setAttribute("category", category);
        session.setAttribute("question", question);
        return "question/question";
    }

    @RequestMapping(
            value = {"/game/chooseAnswer/{id}"},
            method = {RequestMethod.GET}
    )
    public String chooseAnswer(@PathVariable String id, HttpSession session) {
        Question question = (Question)session.getAttribute("question");
        if(questionService.checkAnswer(id, question)){
            questionService.removeQuestion(question);
            gameService.correctAnswer();
            return "redirect:/game/question/" + session.getAttribute("category");
        }
        gameService.wrongAnswer();
        return "redirect:/game/wrongAnswer";
    }

    @RequestMapping(
            value = {"/game/wrongAnswer"},
            method = {RequestMethod.GET}
    )
    public String wrongAnswer(Model model) {
        model.addAttribute("points", gameService.getPoints());
        return "question/wrongAnswer";
    }


}
