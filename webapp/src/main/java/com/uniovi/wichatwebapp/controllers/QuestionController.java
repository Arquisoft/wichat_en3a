package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.dto.AnswerDto;
import com.uniovi.wichatwebapp.services.GameService;
import com.uniovi.wichatwebapp.services.ScoreService;
import entities.QuestionCategory;
import entities.Score;
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

    @RequestMapping(value="/game/categories/start", method = RequestMethod.GET)
    public String createAllCategoriesGame( ) {
        gameService.startAllCategoriesGame();
        return "redirect:/game/question";
    }



    @RequestMapping(value="/play/{id}", method = RequestMethod.GET)
    public String multiplayerGame(@PathVariable String id, Model model) {
        Score score = scoreService.getScore(id);
        if(score == null){
            return "redirect:/home";
        }
        model.addAttribute("otherPlayer", score.getUser());
        model.addAttribute("score", score.getScore());
        model.addAttribute("category", score.getCategory());
        model.addAttribute("questionTime", score.getQuestionTime());
        return "multiplayer/details";
    }

    @RequestMapping(value="/game/multiplayer/start/{id}", method = RequestMethod.GET)
    public String startMultiplayerGame(@PathVariable String id) {
        Score score = scoreService.getScore(id);
        if(score == null){
            return "redirect:/home";
        }
        gameService.start(score.getQuestions(), QuestionCategory.valueOf(score.getCategory()), score.getScore());
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
        model.addAttribute("timer", gameService.getTimer());
        model.addAttribute("questions", gameService.getMaxQuestions());

        Score score;
        if(gameService.getCategory()==null){
            model.addAttribute("category","All topics");
            score = new Score(username, "All topics", gameService.getPoints(), gameService.getRightAnswers(), gameService.getWrongAnswers());
        }
        else {
            model.addAttribute("category", gameService.getCategory().name());
            score = new Score(username, gameService.getCategory().toString(), gameService.getPoints(), gameService.getRightAnswers(), gameService.getWrongAnswers());

        }
        score.setQuestions(gameService.getGame().getQuestionList());
        if(!scoreService.addScore(score)){
            model.addAttribute("addError", true);
        } else{
            model.addAttribute("multiplayerURL", "/play/" + score.getId());
        }
        if(gameService.isMultiplayer()){
            model.addAttribute("isMultiplayer", true);
            model.addAttribute("otherPlayerScore", gameService.getMultiPlayerScore());
        }
        if(gameService.getCategory()==null){
            gameService.startAllCategoriesGame();
        }
        else{
            gameService.start(gameService.getCategory());
        }

        return "question/results";
    }

}