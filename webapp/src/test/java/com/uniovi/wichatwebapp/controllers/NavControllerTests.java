package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.services.GameService;
import com.uniovi.wichatwebapp.services.HintService;
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
public class NavControllerTests {
    @InjectMocks
    private NavController navController;
    
    @Mock
    private Model model;

    @Test
    void addNavigationAttributes_ShouldShowHomeForGamePaths() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/game/question");

        Map<String, Object> modelAttributes = new HashMap<>();
        when(model.addAttribute(eq("showHome"), anyBoolean())).thenAnswer(invocation -> {
            modelAttributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return model;
        });

        // Act
        navController.addNavigationAttributes(request, model);

        // Assert
        verify(model).addAttribute("showHome", true);
        assertThat(modelAttributes).containsEntry("showHome", true);
    }

    @Test
    void addNavigationAttributes_ShouldShowHomeForUserPaths() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/user/profile");

        Map<String, Object> modelAttributes = new HashMap<>();
        when(model.addAttribute(eq("showHome"), anyBoolean())).thenAnswer(invocation -> {
            modelAttributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return model;
        });

        // Act
        navController.addNavigationAttributes(request, model);

        // Assert
        verify(model).addAttribute("showHome", true);
        assertThat(modelAttributes).containsEntry("showHome", true);
    }

    @Test
    void addNavigationAttributes_ShouldNotShowHomeForOtherPaths() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/auth/login");

        Map<String, Object> modelAttributes = new HashMap<>();
        when(model.addAttribute(eq("showHome"), anyBoolean())).thenAnswer(invocation -> {
            modelAttributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return model;
        });

        // Act
        navController.addNavigationAttributes(request, model);

        // Assert
        verify(model).addAttribute("showHome", false);
        assertThat(modelAttributes).containsEntry("showHome", false);
    }

    @Test
    void addNavigationAttributes_ShouldHandleEmptyPath() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("");

        Map<String, Object> modelAttributes = new HashMap<>();
        when(model.addAttribute(eq("showHome"), anyBoolean())).thenAnswer(invocation -> {
            modelAttributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return model;
        });

        // Act
        navController.addNavigationAttributes(request, model);

        // Assert
        verify(model).addAttribute("showHome", false);
        assertThat(modelAttributes).containsEntry("showHome", false);
    }
}
