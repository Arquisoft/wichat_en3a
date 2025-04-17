package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Wordle;
import com.uniovi.wichatwebapp.repositories.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WordleService {

    @Autowired
    private AnswerRepository answerRepository;

    private final Map<String, Wordle> activeGames = new HashMap<>();

    public Wordle startNewGame(String userId) {
        /*List<String> answers = answerRepository.findAnswersByLanguage("en")
                .stream()
                .map(answer -> answer.getText().toUpperCase().trim())
                .filter(word -> word.length() == 5 && word.matches("[A-Z]+"))
                .toList();*/

        List<Answer> test = answerRepository.findAnswersByLanguage("en");
        System.out.println("Encontradas " + test.size() + " respuestas en inglés");

        test.forEach(a -> System.out.println("- " + a.getText()));

        List<String> answers = new ArrayList<String>();

        answers.add("SPAIN");
        answers.add("ITALY");

        if (answers.isEmpty()) {
            throw new RuntimeException("There's no available words");
        }

        String targetWord = answers.get(new Random().nextInt(answers.size()));
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
