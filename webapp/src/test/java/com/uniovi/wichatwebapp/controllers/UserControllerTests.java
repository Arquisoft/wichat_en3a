package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.entities.User;
import com.uniovi.wichatwebapp.services.ScoreService;
import com.uniovi.wichatwebapp.services.UserService;
import com.uniovi.wichatwebapp.validators.SignUpValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private ScoreService scoreService;

    @Mock
    private SignUpValidator signUpValidator;

    @Mock
    private Model model;

    @Test
    void loginTest() {
        String view = userController.login();
        Assertions.assertEquals("login", view);
    }
}
