package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.entities.Wordle;
import com.uniovi.wichatwebapp.services.WordleService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WordleController {

    private final WordleService wordleService;

    public WordleController(WordleService wordleService) {
        this.wordleService = wordleService;
    }

    @RequestMapping(value = "/wordle/start")
    public String startGame() {
        String username = getCurrentUsername();
        wordleService.startNewGame(username);
        return "redirect:/wordle/game";
    }

    @RequestMapping(value = "/wordle/game", method = RequestMethod.GET)
    public String getGame(Model model) {
        String username = getCurrentUsername();
        Wordle game = wordleService.getGame(username);

        // We divide the input in letters
        List<List<Character>> attemptsSplit = game.getAttempts().stream()
                .map(str -> str.chars().mapToObj(c -> (char) c).toList())
                .toList();

        model.addAttribute("wordle", game);
        model.addAttribute("attemptsSplit", attemptsSplit);
        model.addAttribute("feedbackHistory", game.getFeedbackHistory());
        if (wordleService.getGame(username).getRemainingAttempts() == 0 && wordleService.getGame(username).getStatus() == Wordle.GameStatus.LOSE) {
            model.addAttribute("solutionWord", wordleService.getGame(username).getTargetWord());
        }

        return "wordle/game";
    }


    @RequestMapping(value = "/wordle/guess", method = RequestMethod.POST)
    public String makeGuess(@RequestParam String guess) {
        String username = getCurrentUsername();
        if(guess.length() == 5) {
            wordleService.makeGuess(username, guess);
        }

        return "redirect:/wordle/game";
    }

    @RequestMapping(value = "/wordle/reset")
    public String resetGame() {
        String username = getCurrentUsername();
        wordleService.resetGame(username);
        return "redirect:/wordle/start";
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
