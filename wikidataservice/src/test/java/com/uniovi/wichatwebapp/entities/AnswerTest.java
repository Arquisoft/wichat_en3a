package com.uniovi.wichatwebapp.entities;

import entities.Answer;
import entities.AnswerCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnswerTest {

    private Answer answer;

    @BeforeEach
    void setUp() {
        answer = new Answer("Sample Answer", AnswerCategory.FLAG, "France");
    }

    @Test
    void testAnswerCreation() {
        assertNotNull(answer);
        assertEquals("Sample Answer", answer.getText());
        assertEquals("France", answer.getLanguage());
        assertEquals(AnswerCategory.FLAG, answer.getCategory());
        assertNotNull(answer.getId()); // Ensures the ID is generated
    }

    @Test
    void testCategoryAssignment() {
        answer.setCategory(AnswerCategory.FLAG);
        assertEquals(AnswerCategory.FLAG, answer.getCategory());
    }
}
