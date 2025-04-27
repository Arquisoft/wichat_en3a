package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.repositories.ScoreRepository;
import entities.Score;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScoreServiceTests {
    @InjectMocks
    private ScoreService scoreService;

    @Mock
    private ScoreRepository scoreRepository;

    private Score testScore = new Score();

    @Test
    public void addScoreTest() {
        when(scoreRepository.addScore(any())).thenReturn(testScore);

        boolean couldAdd = scoreService.addScore(testScore);
        Assertions.assertEquals(true, couldAdd);
    }

    @Test
    public void removeQuestionTest() {
        List<Score> testsScores = new ArrayList<>();
        when(scoreRepository.getBestScores(any())).thenReturn(testsScores);

        List<Score> score = scoreService.getBestScores("test");
        Assertions.assertEquals(testsScores, score);
    }

}
