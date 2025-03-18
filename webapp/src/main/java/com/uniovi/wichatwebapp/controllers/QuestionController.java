package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.dto.AnswerDto;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.Score;
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
        return "redirect:/game/question";
    }

    @RequestMapping("/game/getAnswer")
    @ResponseBody
    public AnswerDto getAnswer(@RequestParam String answerId){

        int prevPoints = gameService.getPoints();
        gameService.checkAnswer(answerId);

        String correctId = gameService.getCurrentQuestion().getCorrectAnswer().getId();
        int points = gameService.getPoints();

        return new AnswerDto(correctId, points, prevPoints);
    }


    @RequestMapping(value="/game/next")
    public String nextQuestion(Model model) {
        if(gameService.hasGameEnded()){
            return "redirect:/game/results";
        }else{
            gameService.nextQuestion();
            return "redirect:/game/question";
        }
    }

    @RequestMapping(
                value = {"/game/question"},
            method = {RequestMethod.GET}
    )
    public String getQuestion(Model model) {
       model.addAttribute("question", gameService.getCurrentQuestion());
       model.addAttribute("points", gameService.getPoints());
        return "question/question";
    }


    @RequestMapping(value = "/game/results")
    public String results(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("player", username);
        model.addAttribute("points", gameService.getPoints());

        return "question/results";
    }


    @RequestMapping(value="/game/save")
    public String saveScore(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        int points = gameService.getPoints();

        Score score = new Score(username, "Flags", points);
        Score test = scoreService.addScore(score);

        return "redirect:/user/scores";

    }


}
