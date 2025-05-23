package com.uniovi.wichatwebapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
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
    void addNavigationAttributes_ShouldShowHomeForAkiantorPaths() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/akinator/game");

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
    void addNavigationAttributes_ShouldShowHomeForWordlePaths() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/wordle/game");

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
    void addNavigationAttributes_ShouldShowHomeForPlayPaths() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/play/game");

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
    void addNavigationAttributes_ShouldShowHomeForNavigatorPaths() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/akinator/game");

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
