package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.services.AkinatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AkinatorController {
    private final AkinatorService gameService;

    public AkinatorController(AkinatorService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(value="/akinator")
    public String getAkinator(Model model) {
        model.addAttribute("categories", QuestionCategory.values());
        return "akinator/akinatorHome";
    }

    @RequestMapping(value="/akinator", method = RequestMethod.POST)
    public String createGame(Model model, @RequestParam QuestionCategory category, @RequestParam String mode) {
        gameService.start(category, mode);
        return "redirect:/akinator/game";
    }

    @RequestMapping(value="/akinator/game")
    public String getAkinatorGame(Model model) {
        model.addAttribute("ai", gameService.getAiMessage());
        model.addAttribute("aiGuesses", gameService.isAiGuessing());
        return "akinator/game";
    }

    @RequestMapping(value="/akinator/askQuestion", method = RequestMethod.POST)
    public String askQuestion(Model model, @RequestParam String question) {
        gameService.askQuestion(question);
        model.addAttribute("ai", gameService.getAiMessage());
        model.addAttribute("aiGuesses", gameService.isAiGuessing());
        return "redirect:/akinator/game";
    }

    @RequestMapping(value="/akinator/yes", method = RequestMethod.POST)
    public String answeredYes(Model model) {
        gameService.answer("Yes");
        model.addAttribute("ai", gameService.getAiMessage());
        model.addAttribute("aiGuesses", gameService.isAiGuessing());
        return "redirect:/akinator/game";
    }

    @RequestMapping(value="/akinator/probablyYes", method = RequestMethod.POST)
    public String answeredProbablyYes(Model model) {
        gameService.answer("Probably yes");
        model.addAttribute("ai", gameService.getAiMessage());
        model.addAttribute("aiGuesses", gameService.isAiGuessing());
        return "redirect:/akinator/game";
    }

    @RequestMapping(value="/akinator/dontKnow", method = RequestMethod.POST)
    public String answeredDontKnow(Model model) {
        gameService.answer("No");
        model.addAttribute("ai", gameService.getAiMessage());
        model.addAttribute("aiGuesses", gameService.isAiGuessing());
        return "redirect:/akinator/game";
    }

    @RequestMapping(value="/akinator/probablyNo", method = RequestMethod.POST)
    public String answeredProbablyNo(Model model) {
        gameService.answer("Probably no");
        model.addAttribute("ai", gameService.getAiMessage());
        model.addAttribute("aiGuesses", gameService.isAiGuessing());
        return "redirect:/akinator/game";
    }

    @RequestMapping(value="/akinator/no", method = RequestMethod.POST)
    public String answeredNo(Model model) {
        gameService.answer("No");
        model.addAttribute("ai", gameService.getAiMessage());
        model.addAttribute("aiGuesses", gameService.isAiGuessing());
        return "redirect:/akinator/game";
    }
}
