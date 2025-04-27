package com.uniovi.wichatwebapp.controller;


import com.uniovi.wichatwebapp.service.QuestionService;
import entities.Answer;
import entities.AnswerCategory;
import entities.Question;
import entities.QuestionCategory;
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
    void getQuestionTest_ReturnsRandomQuestion() {
        // Arrange
        Answer correctAnswer = new Answer("Correct answer", AnswerCategory.FLAG,"en");
        Question expectedQuestion = new Question(correctAnswer, "Sample question", "image.jpg", QuestionCategory.GEOGRAPHY);
        when(questionService.getRandomQuestion("en",QuestionCategory.GEOGRAPHY)).thenReturn(expectedQuestion);

        // Act
        Question result = questionController.getQuestion(QuestionCategory.GEOGRAPHY);

        // Assert
        assertThat(result).isEqualTo(expectedQuestion);
        verify(questionService).getRandomQuestion("en",QuestionCategory.GEOGRAPHY);
    }

    @Test
    void getCorrectAnswerTest_ReturnsCorrectAnswerForQuestion() {
        // Arrange
        Answer correctAnswer = new Answer("Correct answer", AnswerCategory.FLAG,"en");
        Question question = new Question(correctAnswer, "Sample question", "image.jpg",QuestionCategory.GEOGRAPHY);
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
    void getAnswerTest_ReturnsAnswerById() {
        // Arrange
        Answer expectedAnswer = new Answer("Test answer", AnswerCategory.FLAG,"en");
        String answerId = expectedAnswer.getId();
        when(questionService.findAnswerById(answerId)).thenReturn(expectedAnswer);

        // Act
        Answer result = questionController.getAnswer(answerId);

        // Assert
        assertThat(result).isEqualTo(expectedAnswer);
        verify(questionService).findAnswerById(answerId);
    }

    @Test
    void removeQuestionTest_DeletesQuestionById() {
        // Arrange
        String questionId = "q1";
        Answer correctAnswer = new Answer("Correct",AnswerCategory.FLAG, "en");
        Question question = new Question(correctAnswer, "Content", "image.jpg",QuestionCategory.GEOGRAPHY);
        question.setId(questionId);

        when(questionService.findQuestionById(questionId)).thenReturn(question);

        // Act
        questionController.removeQuestion(questionId);

        // Assert
        verify(questionService).findQuestionById(questionId);
        verify(questionService).removeQuestion(question);
    }



    @Test
    void getRandomCategoryQuestionTest_ReturnsRandomQuestion() {
        // Arrange
        Answer correctAnswer = new Answer("Correct answer", AnswerCategory.FLAG, "en");
        Question expectedQuestion = new Question(correctAnswer, "Sample question", "image.jpg", QuestionCategory.GEOGRAPHY);
        when(questionService.getRandomQuestionNoCategory("en")).thenReturn(expectedQuestion);

        // Act
        Question result = questionController.getRandomCategoryQuestion();

        // Assert
        assertThat(result).isEqualTo(expectedQuestion);
        verify(questionService).getRandomQuestionNoCategory("en");
    }




}