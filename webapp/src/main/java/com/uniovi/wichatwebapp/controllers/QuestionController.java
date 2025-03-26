package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.dto.AnswerDto;
import com.uniovi.wichatwebapp.entities.Question;
<<<<<<< HEAD
import com.uniovi.wichatwebapp.entities.QuestionCategory;
=======
import com.uniovi.wichatwebapp.entities.Score;
>>>>>>> development-gameservice-manuel
import com.uniovi.wichatwebapp.services.GameService;
import com.uniovi.wichatwebapp.services.QuestionService;
import com.uniovi.wichatwebapp.services.ScoreService;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.Random;

@Controller
public class QuestionController {
    private final GameService gameService;
    private final ScoreService scoreService;

    public QuestionController(GameService gameService, ScoreService scoreService) {
        this.gameService = gameService;
        this.scoreService = scoreService;
    }

    @RequestMapping(value="/game/start")
    public String startGame(Model model) {
        gameService.start();
        gameService.nextQuestion();
        return "redirect:/game/question";
    }

    @RequestMapping("/game/getAnswer")
    @ResponseBody
    public AnswerDto getAnswer(@RequestParam String answerId){

        int prevPoints = gameService.getPoints();
        gameService.checkAnswer(answerId);

        String correctId = gameService.getCurrentQuestion().getCorrectAnswer().getId();
        int points = gameService.getPoints();

        gameService.nextQuestion();

        return new AnswerDto(correctId, points, prevPoints);
    }


    @RequestMapping(value="/game/next")
    public String nextQuestion() {
        if(gameService.hasGameEnded()){
            return "redirect:/game/results";
        }else{
            return "redirect:/game/question";
        }
    }

    @RequestMapping(
            value = {"/game/question"},
            method = {RequestMethod.GET}
    )
<<<<<<< HEAD
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
=======
    public String getQuestion(Model model) {
       model.addAttribute("question", gameService.getCurrentQuestion());
       model.addAttribute("points", gameService.getPoints());
       model.addAttribute("timer", gameService.getTimer());
        return "question/question";
    }

    @RequestMapping(value="/game/timeout")
    public String timeout() {
>>>>>>> development-gameservice-manuel
        gameService.wrongAnswer();
        return "redirect:/game/next";
    }


    @RequestMapping(value = "/game/results")
    public String results(Model model) {

        if(!gameService.hasGameEnded()){
            return "redirect:/game/start";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("player", username);
        model.addAttribute("points", gameService.getPoints());
        model.addAttribute("right", gameService.getRightAnswers());
        model.addAttribute("wrong", gameService.getWrongAnswers());

        Score score = new Score(username, "Flags", gameService.getPoints(), gameService.getRightAnswers(), gameService.getWrongAnswers());
        scoreService.addScore(score);

        gameService.start();

        return "question/results";
    }




}
