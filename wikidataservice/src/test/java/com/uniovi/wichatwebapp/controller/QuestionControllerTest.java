package com.uniovi.wichatwebapp.controller;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    @Test
    void getQuestion_ReturnsRandomQuestion() {
        // Arrange
        Answer correctAnswer = new Answer("Correct answer", "en");
        Question expectedQuestion = new Question(correctAnswer, "Sample question", "image.jpg");
        when(questionService.getRandomQuestion("en")).thenReturn(expectedQuestion);

        // Act
        Question result = questionController.getQuestion();

        // Assert
        assertThat(result).isEqualTo(expectedQuestion);
        verify(questionService).getRandomQuestion("en");
    }

    @Test
    void getCorrectAnswer_ReturnsCorrectAnswerForQuestion() {
        // Arrange
        Answer correctAnswer = new Answer("Correct answer", "en");
        Question question = new Question(correctAnswer, "Sample question", "image.jpg");
        String questionId = "q1";
        question.setId(questionId);

        when(questionService.findQuestionById(questionId)).thenReturn(question);
        when(questionService.findAnswerById(correctAnswer.getId())).thenReturn(correctAnswer);

        // Act
        Answer result = questionController.getCorrectAnswer(questionId);

        // Assert
        assertThat(result).isEqualTo(correctAnswer);
        verify(questionService).findQuestionById(questionId);
        verify(questionService).findAnswerById(correctAnswer.getId());
    }

    @Test
    void getAnswer_ReturnsAnswerById() {
        // Arrange
        Answer expectedAnswer = new Answer("Test answer", "en");
        String answerId = expectedAnswer.getId();
        when(questionService.findAnswerById(answerId)).thenReturn(expectedAnswer);

        // Act
        Answer result = questionController.getAnswer(answerId);

        // Assert
        assertThat(result).isEqualTo(expectedAnswer);
        verify(questionService).findAnswerById(answerId);
    }

    @Test
    void removeQuestion_DeletesQuestionById() {
        // Arrange
        String questionId = "q1";
        Answer correctAnswer = new Answer("Correct", "en");
        Question question = new Question(correctAnswer, "Content", "image.jpg");
        question.setId(questionId);

        when(questionService.findQuestionById(questionId)).thenReturn(question);

        // Act
        questionController.removeQuestion(questionId);

        // Assert
        verify(questionService).findQuestionById(questionId);
        verify(questionService).removeQuestion(question);
    }
}