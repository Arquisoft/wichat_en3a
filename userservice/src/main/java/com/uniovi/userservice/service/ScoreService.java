package com.uniovi.userservice.service;


import com.uniovi.userservice.repository.ScoreRepository;
import entities.Score;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public ScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public void addScore(Score score) {
        scoreRepository.save(score);
    }

    public List<Score> findBestScores(String user) {
        Sort sort = Sort.by(Sort.Direction.DESC, "score");
        List<Score> scores = scoreRepository.findBestByUser(sort, user);
        return scores.size() > 10 ? scores.subList(0, 10) : scores;
    }

    public Score findScore(String id){
        return scoreRepository.findById(id).orElse(null);
    }
}
