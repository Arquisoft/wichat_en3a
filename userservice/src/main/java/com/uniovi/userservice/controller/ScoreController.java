package com.uniovi.userservice.controller;

import com.uniovi.userservice.entities.Score;
import com.uniovi.userservice.entities.User;
import com.uniovi.userservice.service.ScoreService;
import com.uniovi.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
public class ScoreController {

    private final ScoreService scoreService;
    private final UserService userService;

    public ScoreController(ScoreService scoreService, UserService userService) {
        this.scoreService = scoreService;
        this.userService = userService;
    }


    @Operation(summary = "Adds the score to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Score was successfully added",
                content = {
                    @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Score.class))
                }
            ),
            @ApiResponse(responseCode = "409", description = "Score could not be added", content = @Content)
    })
    @RequestMapping(value="/addScore", method = RequestMethod.POST)
    public Score addScore(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Score to be added", required = true,
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Score.class),
            examples = @ExampleObject(value = "{ \"user\": \"prueba@prueba.com\"," +
                    "\"category\": \"Flags\"," +
                    "\"score\": \"800\"," +
                    "\"rightAnswers\": \"9\"," +
                    "\"wrongAnswers\": \"1\"}"))
        )
            @RequestBody Score score) {

        User user = userService.findByEmail(score.getUser());
        if(user.isCorrect()){
            score.setUser_id(user.getId());
            scoreService.addScore(score);
        }else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with that email could not be found");
        }

        return score;

    }

    @Operation(summary = "Gets the 10 best scores of the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a list with the 10 best scores",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Score.class)))}),
    })
    @RequestMapping(value = "/getBestScores", method = RequestMethod.GET)
    public List<Score> getBestScores(@RequestParam String user) {
        return scoreService.findBestScores(user);
    }

}
