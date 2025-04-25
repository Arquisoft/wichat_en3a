package com.uniovi.userservice.controller;

import com.uniovi.userservice.service.UserService;
import entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * RestController that manages the users
 * Manages:
 * -Adding users to the database
 * -Getting users from the database
 */
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Adds a user to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was added successfully",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = User.class),
            examples = @ExampleObject(value = "{ \"id\": \"1\"," +
                    "\"name\": \"Antonio\"," +
                    "\"email\": \"prueba@prueba.com\"," +
                    "\"password\": \"pruebaPW\"}"))}),
            @ApiResponse(responseCode = "409", description = "User with that email already exists", content = @Content),

    })
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public User addUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User to be added", required = true,
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = User.class),
            examples = @ExampleObject(value = "{ \"name\": \"Antonio\"," +
                    "\"email\": \"prueba@prueba.com\"," +
                    "\"password\": \"pruebaPW\"}"))
        )
            @RequestBody User user) {

        User newUser = userService.addUser(user);

        if(!newUser.isCorrect()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with that email already exists");
        }

        return newUser;
    }

    @Operation(summary = "Finds the user by the email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the user matching the email",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "404", description = "User was not found", content = @Content)
    })
    @RequestMapping(value="/getUser", method = RequestMethod.GET)
    public User findUser(
            @Parameter(description = "Email of the user")
            @RequestParam String email){
        //Get the user by the email
        User user = userService.findByEmail(email);

        if(!user.isCorrect()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with that email was not found");
        }

        return user;

    }

}
