package com.uniovi.wichatwebapp.entitites;

import entities.Answer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AnswerTest {
    @Test
    void fullConstructorTest() {
        // Arrange & Act
        Answer answer = new Answer("Madrid", "en");

        // Assert
        Assertions.assertEquals("Madrid", answer.getText());
        Assertions.assertEquals("en", answer.getLanguage());
        Assertions.assertNull(answer.getId());
    }

    @Test
    void defaultConstructorTest() {
        // Arrange & Act
        Answer answer = new Answer();

        // Assert
        Assertions.assertNull(answer.getText());
        Assertions.assertNull(answer.getLanguage());
        Assertions.assertNull(answer.getId());
    }

    @Test
    void settersAndGettersTest() {
        // Arrange
        Answer answer = new Answer();

        // Act
        answer.setId("123");

        // Assert
        Assertions.assertEquals("123", answer.getId());
    }
}
