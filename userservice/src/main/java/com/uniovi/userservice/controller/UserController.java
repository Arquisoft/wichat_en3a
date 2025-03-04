package com.uniovi.userservice.controller;

import com.uniovi.userservice.entities.User;
import com.uniovi.userservice.service.UserService;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public User addUser(@RequestBody User user) {
        //Get the user by the email
        User newUser = userService.addUser(user);

        return newUser;
    }

    @RequestMapping(value="/getUser", method = RequestMethod.GET)
    public User findUser(@RequestParam String email){
        //Get the user by the email
        User user = userService.findByEmail(email);

        return user;

    }

}
