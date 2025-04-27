package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.repositories.ScoreRepository;
import entities.Score;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public ScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public boolean addScore(Score score) {
        return scoreRepository.addScore(score) != null;
    }

    public Score addAndGetScore(Score score) {
        return scoreRepository.addScore(score);
    }

    public List<Score> getBestScores(String email) {
        return scoreRepository.getBestScores(email);
    }

    public Score getScore(String id) {
        return scoreRepository.getScore(id);
    }
}
