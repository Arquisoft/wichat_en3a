package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.entities.Wordle;
import com.uniovi.wichatwebapp.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WordleService {

    @Autowired
    private QuestionRepository questionRepository;

    private final Map<String, Wordle> activeGames = new HashMap<>();

    public Wordle startNewGame(String userId) {
        String targetWord = "";
        while(targetWord.length() != 5) {
            targetWord = questionRepository.getRandomQuestion(QuestionCategory.GEOGRAPHY).getCorrectAnswer().getText();
        }
        Wordle game = new Wordle(targetWord);
        activeGames.put(userId, game);
        return game;
    }

    public Wordle makeGuess(String userId, String guess) {
        Wordle game = activeGames.get(userId);
        if (game == null) {
            throw new RuntimeException("There's no active game");
        }

        game.guess(guess.toUpperCase());
        return game;
    }

    public Wordle getGame(String userId) {
        return activeGames.get(userId);
    }

    public void resetGame(String userId) {
        activeGames.remove(userId);
    }
}
