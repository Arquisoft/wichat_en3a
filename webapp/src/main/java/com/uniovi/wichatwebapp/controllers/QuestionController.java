package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.dto.AnswerDto;
import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.entities.Score;
import com.uniovi.wichatwebapp.services.GameService;
import com.uniovi.wichatwebapp.services.ScoreService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class QuestionController {
    private final GameService gameService;
    private final ScoreService scoreService;

    public QuestionController(GameService gameService, ScoreService scoreService) {
        this.gameService = gameService;
        this.scoreService = scoreService;
    }

    @RequestMapping(value="/game/personalized")
    public String getPersonalizedGame(Model model) {
        model.addAttribute("categories", QuestionCategory.values());
        return "personalized";
    }

    @RequestMapping(value="/game/personalized", method = RequestMethod.POST)
    public String createPersonalizedGame(@RequestParam QuestionCategory category, @RequestParam int timerSeconds, @RequestParam int questionCount) {
        gameService.start(category, timerSeconds, questionCount);
        return "redirect:/game/question";
    }


    @RequestMapping(value="/game/start/{category}")
    public String startGame(@PathVariable QuestionCategory category) {
        gameService.start(category);
        return "redirect:/game/question";
    }

    @RequestMapping(value="/game/start/{category}/{timer}/{questions}")
    public String startGame(@PathVariable QuestionCategory category, @PathVariable int timer, @PathVariable int questions) {
        gameService.start(category, timer, questions);
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
    public String getQuestion(Model model) {
       model.addAttribute("question", gameService.getCurrentQuestion());
       model.addAttribute("points", gameService.getPoints());
       model.addAttribute("timer", gameService.getTimer());
        return "question/question";
    }

    @RequestMapping(value="/game/timeout")
    public String timeout() {
        gameService.wrongAnswer();
        return "redirect:/game/next";
    }


    @RequestMapping(value = "/game/results")
    public String results(Model model) {

        if(!gameService.hasGameEnded()){
            return "redirect:/game/start/" + gameService.getCategory().name();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("player", username);
        model.addAttribute("points", gameService.getPoints());
        model.addAttribute("right", gameService.getRightAnswers());
        model.addAttribute("wrong", gameService.getWrongAnswers());
        model.addAttribute("category", gameService.getCategory().name());

        model.addAttribute("timer", gameService.getTimer());
        model.addAttribute("questions", gameService.getMaxQuestions());

        Score score = new Score(username, gameService.getCategory().toString(), gameService.getPoints(), gameService.getRightAnswers(), gameService.getWrongAnswers());
        if(!scoreService.addScore(score)){
            model.addAttribute("addError", true);
        }

        gameService.start(gameService.getCategory());

        return "question/results";
    }




}