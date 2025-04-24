package com.uniovi.wichatwebapp.controllers;

import com.uniovi.wichatwebapp.services.AkinatorService;
import com.uniovi.wichatwebapp.services.GameService;
import com.uniovi.wichatwebapp.services.HintService;
import entities.Answer;
import entities.Question;
import entities.QuestionCategory;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AkinatorControllerTests {
    @InjectMocks
    private AkinatorController akinatorController;

    @Mock
    private AkinatorService akinatorService;

    @Mock
    private Model model;

    @Test
    void getAkinatorTest() {
        // Arrange
        Map<String, Object> modelAttributes = new HashMap<>();
        when(model.addAttribute(eq("categories"), any())).thenAnswer(invocation -> {
            modelAttributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return model;
        });

        // Act
        String view = akinatorController.getAkinator(model);

        // Assert
        verify(model).addAttribute("categories", QuestionCategory.values());
        assertThat(modelAttributes).containsKey("categories");
        assertThat(modelAttributes.get("categories")).isEqualTo(QuestionCategory.values());

        Assertions.assertEquals("personalized", view);
    }
}
