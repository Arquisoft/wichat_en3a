package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.repositories.HintRepository;
import com.uniovi.wichatwebapp.services.GameService;
import com.uniovi.wichatwebapp.services.HintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class HintController {

    @Autowired
    private GameService game;

    @Autowired
    private HintService hintService;

    @GetMapping("/hint")
    @ResponseBody
    public String getHint(@RequestParam String question) {
        return hintService.askQuestionToIA(game.getCurrentQuestion(), question);
    }


}
