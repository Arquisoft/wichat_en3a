package com.uniovi.userservice.controller;

import com.uniovi.userservice.entities.Score;
import com.uniovi.userservice.entities.User;
import com.uniovi.userservice.service.ScoreService;
import com.uniovi.userservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScoreController {

    private final ScoreService scoreService;
    private final UserService userService;

    public ScoreController(ScoreService scoreService, UserService userService) {
        this.scoreService = scoreService;
        this.userService = userService;
    }


    @RequestMapping(value="/addScore", method = RequestMethod.POST)
    public Score addScore(@RequestBody Score score) {

        User user = userService.findByEmail(score.getUser());
        if(user.isCorrect()){
            score.setUser_id(user.getId());
            scoreService.addScore(score);
        }

        return score;

    }

    @RequestMapping(value = "/getBestScores")
    public List<Score> getBestScores(@RequestParam String user) {
        return scoreService.findBestScores(user);
    }

}
