package com.uniovi.userservice.controllers;


import com.uniovi.userservice.controller.ScoreController;

import com.uniovi.userservice.service.ScoreService;
import com.uniovi.userservice.service.UserService;
import com.uniovi.userservice.errorHandling.exceptions.*;
import entities.Score;
import entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoreControllerTests {

    @InjectMocks
    private ScoreController scoreController;

    @Mock
    private ScoreService scoreService;

    @Mock
    private UserService userService;


    @Test
    void testAddScore() {
        //Test data
        Score s = new Score("test@test.com", "Flags", 800, 8, 2);
        User u = new User("1", "Test", "test@test.com", "testPw", true);

        //Mock user service response
        when(userService.findByEmail("test@test.com")).thenReturn(u);

        //Execute method
        Score result = scoreController.addScore(s);

        //Check results
        assertEquals(u.getId(), result.getUser_id());

    }

    @Test
    void testAddScoreIncorrectUser(){
        //Test data
        Score s = new Score("test@test.com", "Flags", 800, 8, 2);
        User u = new User("1", "test", "test@test.com", "testPw", false);

        //Mock user service
        when(userService.findByEmail("test@test.com")).thenReturn(u);

        //Execute method
        try{
            Score result = scoreController.addScore(s); //must fail and throw exception
            fail();
        } catch (UserNotFoundException e) {
            assertEquals("User with email test@test.com not found", e.getMessage());
        }


    }

    @Test
    void testGetScores(){
        //Test data
        Score s1 = new Score("test@test.com", "Flags", 800, 8, 2);
        Score s2 = new Score("test@test.com", "Flags", 900, 9, 1);

        //Mock score service response
        when(scoreService.findBestScores("test@test.com")).thenReturn(List.of(s1,s2));

        //Execute controller method
        List<Score> results = scoreController.getBestScores("test@test.com");

        //Check results
        assertFalse(results.isEmpty());
        assertEquals(2, results.size());

        assertEquals(s1, results.get(0));
        assertEquals(s2, results.get(1));

    }

}
