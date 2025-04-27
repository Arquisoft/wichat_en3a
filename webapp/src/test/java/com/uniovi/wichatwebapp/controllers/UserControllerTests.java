package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.services.ScoreService;
import com.uniovi.wichatwebapp.services.UserService;
import com.uniovi.wichatwebapp.validators.SignUpValidator;
import entities.Score;
import entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Authentication authentication;

    @Test
    void loginTest() {
        String view = userController.login();
        Assertions.assertEquals("login", view);
    }

    @Test
    void signupTest() {
        // Arrange
        Map<String, Object> modelAttributes = new HashMap<>();

        // Mock model.addAttribute to capture the User object
        when(model.addAttribute(eq("user"), any(User.class))).thenAnswer(invocation -> {
            modelAttributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return model;
        });

        // Act
        String view = userController.signup(model);

        // Verify the captured model attributes
        assertThat(modelAttributes)
                .containsOnlyKeys("user")
                .extractingByKey("user")
                .isInstanceOf(User.class)
                .satisfies(user -> {
                    User newUser = (User) user;
                    assertThat(newUser.getEmail()).isNull();
                    assertThat(newUser.getPassword()).isNull();
                    // Add more assertions for default User state if needed
                });

        // Verify view name
        Assertions.assertEquals("signup", view);
    }

    @Test
    void signupWithErrorTest() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String view = userController.signup(user, model, bindingResult);

        // Assert
        verify(signUpValidator).validate(user, bindingResult);
        verifyNoInteractions(userService);
        Assertions.assertEquals("signup", view);
    }

    @Test
    void signupWithUserExistsTest() {
        // Arrange
        User user = new User();
        user.setEmail("existing@example.com");
        user.setPassword("password123");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.addUser(user)).thenReturn(false);

        // Act
        String view = userController.signup(user, model, bindingResult);

        // Assert
        verify(signUpValidator).validate(user, bindingResult);
        verify(userService).addUser(user);
        verify(model).addAttribute("adderror", true);
        Assertions.assertEquals("signup", view);
    }

    @Test
    void signupValidUserTest() {
        // Arrange
        User user = new User();
        user.setEmail("new@example.com");
        user.setPassword("password123");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.addUser(user)).thenReturn(true);

        // Act
        String view = userController.signup(user, model, bindingResult);

        // Assert
        verify(signUpValidator).validate(user, bindingResult);
        verify(userService).addUser(user);
        verifyNoInteractions(model); // Should not add error attribute
        Assertions.assertEquals("redirect:login", view);
    }

    @Test
    void homeTest() {
        // Arrange
        String email = "user@example.com";
        String name= "Test User";
        String password = "password123";

        User user = new User(name, email, password, true);

        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.clearContext();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication); // your mocked auth
        SecurityContextHolder.setContext(context);
        when(userService.getUserByEmail(email)).thenReturn(user);

        Map<String, Object> modelAttributes = new HashMap<>();
        when(model.addAttribute(eq("user"), any(User.class))).thenAnswer(invocation -> {
            modelAttributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return model;
        });

        // Act
        userController.home(model);

        // Assert
        User modelUser = (User) modelAttributes.get("user");
        assertThat(modelUser)
                .isNotNull()
                .extracting(User::getEmail, User::getName, User::getPassword)
                .containsExactly(email, name, password);
    }

    @Test
    void baseUrlTest() {
        // Act
        String view = userController.baseUrl();

        Assertions.assertEquals("redirect:home", view);
    }

    @Test
    void scoresTest() {
        // Arrange
        String userEmail = "user@example.com";
        List<Score> mockScores = Arrays.asList(
                new Score(userEmail, "GEOGRAPHY", 100, 5, 2),
                new Score(userEmail, "HISTORY", 85, 4, 3)
        );

        when(authentication.getName()).thenReturn(userEmail);
        SecurityContextHolder.clearContext();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication); // your mocked auth
        SecurityContextHolder.setContext(context);

        when(scoreService.getBestScores(userEmail)).thenReturn(mockScores);

        Map<String, Object> modelAttributes = new HashMap<>();
        when(model.addAttribute(eq("scores"), anyList())).thenAnswer(invocation -> {
            modelAttributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return model;
        });

        // Act
        String viewName = userController.scores(model);

        // Assert
        verify(authentication).getName();
        verify(scoreService).getBestScores(userEmail);

        assertThat(modelAttributes)
                .containsOnlyKeys("scores")
                .extractingByKey("scores")
                .isEqualTo(mockScores);

        Assertions.assertEquals("user/scores", viewName);
    }
}
