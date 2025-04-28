package com.uniovi.wichatwebapp.entities;

import entities.Answer;
import entities.AnswerCategory;
import entities.Question;
import entities.QuestionCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    private Question question;
    private Answer correctAnswer;

    @BeforeEach
    void setUp() {
        correctAnswer = new Answer("This is the correct answer", AnswerCategory.FLAG, "France");
        question = new Question(correctAnswer, "What country does this flag belong to?", "image.png", QuestionCategory.GEOGRAPHY);
    }

    @Test
    void testQuestionCreation() {
        assertNotNull(question);
        assertEquals("What country does this flag belong to?", question.getContent());
        assertEquals(correctAnswer, question.getCorrectAnswer());
    }

    @Test
    void testAnswers() {
        assertNotNull(question.getAnswers());
        assertTrue(question.getAnswers().contains(correctAnswer));
    }

    @Test
    void testCategory() {
        assertEquals(QuestionCategory.GEOGRAPHY, question.getCategory());
    }

    @Test
    void testSetters(){
        question = new Question();
        question.setContent("New content");
        question.setCorrectAnswer(correctAnswer);
        question.setCategory(QuestionCategory.GEOGRAPHY);
        assertEquals("New content", question.getContent());
        assertEquals(correctAnswer, question.getCorrectAnswer());
        assertEquals(QuestionCategory.GEOGRAPHY, question.getCategory());
    }
}
