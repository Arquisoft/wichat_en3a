package com.uniovi.wichatwebapp.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AnswerDtoTests {

    @Test
    void constructorTest() {
        // Arrange
        String expectedId = "123";
        int expectedPoints = 100;
        int expectedPrevPoints = 50;

        // Act
        AnswerDto dto = new AnswerDto(expectedId, expectedPoints, expectedPrevPoints);

        // Assert
        Assertions.assertEquals(expectedId, dto.getCorrectId());
        Assertions.assertEquals(expectedPoints, dto.getPoints());
        Assertions.assertEquals(expectedPrevPoints, dto.getPrevPoints());
    }

    @Test
    void settersAndGettersTest() {
        // Arrange
        AnswerDto dto = new AnswerDto("", 0, 0);
        String newId = "456";
        int newPoints = 200;
        int newPrevPoints = 150;

        // Act
        dto.setCorrectId(newId);
        dto.setPoints(newPoints);
        dto.setPrevPoints(newPrevPoints);

        // Assert
        Assertions.assertEquals(newId, dto.getCorrectId());
        Assertions.assertEquals(newPoints, dto.getPoints());
        Assertions.assertEquals(newPrevPoints, dto.getPrevPoints());
    }
}
