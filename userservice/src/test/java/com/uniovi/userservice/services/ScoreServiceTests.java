package com.uniovi.userservice.services;


import com.uniovi.userservice.entities.Score;
import com.uniovi.userservice.repository.ScoreRepository;
import com.uniovi.userservice.service.ScoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoreServiceTests {

    @InjectMocks
    private ScoreService scoreService;

    @Mock
    private ScoreRepository scoreRepo;

    //Check that it properly returns the results
    @Test
    void testGetBestResults(){
        //Test data
        Score s1 = new Score("test@test.com", "Flags", 800, 8, 2);
        Score s2 = new Score("test@test.com", "Flags", 900, 9, 1);
        Score s3 = new Score("test@test.com", "Flags", 700, 10, 0);

        //Mock repository response
        when(scoreRepo.findBestByUser(Sort.by(Sort.Direction.DESC, "score"), "test@test.com")).thenReturn(List.of(s1, s2, s3));

        //Execute method
        List<Score> results = scoreService.findBestScores("test@test.com");

        //Check results

        assertFalse(results.isEmpty());
        assertEquals(3, results.size());

        assertEquals(s1, results.get(0));
        assertEquals(s2, results.get(1));
        assertEquals(s3, results.get(2));

    }

    //Check that it only returns 10 results max
    @Test
    void testGetBestScoresLimit10(){
        List<Score> scores = new ArrayList<>();

        for(int i = 0; i < 11; i++){
            scores.add(new Score("test@test.com", "Flags", 800, 8, 2));
        }

        when(scoreRepo.findBestByUser(Sort.by(Sort.Direction.DESC, "score"), "test@test.com")).thenReturn(scores);

        List<Score> results = scoreService.findBestScores("test@test.com");

        assertFalse(results.isEmpty());
        assertEquals(10, results.size());

    }

}
