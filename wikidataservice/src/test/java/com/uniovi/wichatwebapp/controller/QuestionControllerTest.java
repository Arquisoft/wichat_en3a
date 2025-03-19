package com.uniovi.wichatwebapp.controller;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    void createQuestion_CreatesCorrectQuestion() {
        String questionId = "q1";
        Answer correctAnswer = new Answer("Correct", "en");
        Question question = new Question(correctAnswer, "Content", "image.jpg");
        question.setId(questionId);

        verify(questionService).save(question);
    }

    @Test
    void createQuestion_CreatesDuplicatedQuestion() {
        String questionId = "q1";
        Answer correctAnswer = new Answer("Correct", "en");
        Question question = new Question(correctAnswer, "Content", "image.jpg");
        question.setId(questionId);
        questionService.save(question);

        verify(questionService).save(question);

    }

    @Test
    void createAnswer_CreatesCorrectAnswers() {
        Answer a1 = new Answer("Correct", "en");
        Answer a2 = new Answer("Incorrect", "en");
        Answer a3 = new Answer("Maybe", "en");
        List<Answer> answers = new ArrayList<>();
        answers.add(a1);
        answers.add(a2);
        answers.add(a3);


        verify(questionService).saveAllAnswers(answers);
    }


    @Test
    void createAnswer_CreatesDuplicatedAnswer() {

        Answer a1 = new Answer("Correct", "en");
        Answer a2 = new Answer("Correct", "en");


        List<Answer> answers = new ArrayList<>();
        answers.add(a1);
        answers.add(a2);

        verify(questionService).saveAllAnswers(answers);
    }

    @Test
    void assignAnswers_CorrectAnswers() {

        String questionId1 = "q1";
        Answer correctAnswer1 = new Answer("Correct1", "en");
        Question question1 = new Question(correctAnswer1, "Content", "image.jpg");
        question1.setId(questionId1);

        Answer incorrectAnswer1 = new Answer("Incorrect", "en");
        Answer incorrectAnswer2 = new Answer("Maybe", "en");
        Answer incorrectAnswer3 = new Answer("Not this", "en");



        List<Answer> answers = new ArrayList<>();
        answers.add(correctAnswer1);
        answers.add(incorrectAnswer1);
        answers.add(incorrectAnswer2);
        answers.add(incorrectAnswer3);

        questionService.save(question1);
        questionService.saveAllAnswers(answers);
        questionService.assignAnswers();

        Question question3 = questionService.findQuestionById(questionId1);
        assertThat(question3.getAnswers().size()).isEqualTo(4);
        assertThat(question3.getCorrectAnswerId()).isEqualTo(correctAnswer1.getId());


    }

    @Test
    void saveAllAnswers() {

        Answer a1 = new Answer("Correct", "en");
        Answer a2 = new Answer("Incorrect", "en");
        Answer a3 = new Answer("Maybe", "en");

        List<Answer> answers = new ArrayList<>();
        answers.add(a1);
        answers.add(a2);
        answers.add(a3);

        questionService.saveAllAnswers(answers);
        assertThat(questionService.findAnswerById(a1.getId())).isEqualTo(a1);
        assertThat(questionService.findAnswerById(a2.getId())).isEqualTo(a2);
        assertThat(questionService.findAnswerById(a3.getId())).isEqualTo(a3);
    }



    @Test
    void saveAllQuestions() {
        String questionId1 = "q1";
        Answer correctAnswer1 = new Answer("Correct1", "en");
        Question question1 = new Question(correctAnswer1, "Content", "image.jpg");
        question1.setId(questionId1);


        String questionId2 = "q2";
        Answer correctAnswer2 = new Answer("Correct2", "en");
        Question question2 = new Question(correctAnswer2, "Content", "image.jpg");
        question2.setId(questionId2);



        String questionId3 = "q3";
        Answer correctAnswer3 = new Answer("Correct3", "en");
        Question question3 = new Question(correctAnswer3, "Content", "image.jpg");
        question3.setId(questionId3);

        List<Question> questions = new ArrayList<>();
        questions.add(question1);
        questions.add(question2);
        questions.add(question3);

        questionService.saveAllQuestions(questions);

        assertThat(questionService.findQuestionById(questionId1)).isEqualTo(question1);
        assertThat(questionService.findQuestionById(questionId2)).isEqualTo(question2);
        assertThat(questionService.findQuestionById(questionId3)).isEqualTo(question3);

    }
}