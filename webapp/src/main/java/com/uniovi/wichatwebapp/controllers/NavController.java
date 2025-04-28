package com.uniovi.wichatwebapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@ControllerAdvice
public class NavController {

    @ModelAttribute
    public void addNavigationAttributes(HttpServletRequest request, Model model) {
        String path = request.getServletPath();
        boolean showHome = path.startsWith("/game") || path.startsWith("/user") || path.startsWith("/akinator") || path.startsWith("/wordle") || path.startsWith("/dictionary") || path.startsWith("/play");
        model.addAttribute("showHome", showHome);
    }
}