package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.entities.User;
import com.uniovi.wichatwebapp.services.UserService;
import com.uniovi.wichatwebapp.validators.SignUpValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {
    private final UserService userService;
    private final SignUpValidator signUpValidator;

    public UserController(UserService userService, SignUpValidator signUpValidator) {
        this.userService = userService;
        this.signUpValidator = signUpValidator;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value="/signup", method = RequestMethod.GET)
    public String signup(Model model){
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@Validated User user, Model model, BindingResult result){
        signUpValidator.validate(user, result);
        if(result.hasErrors()){
            return "signup";
        }
        boolean addResult = userService.addUser(user);
        if(!addResult){
            model.addAttribute("adderror", true);
            return "signup";
        }
        return "redirect:login";
    }

    @RequestMapping(value = "/home")
    public String home(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        return "home";
    }
}
