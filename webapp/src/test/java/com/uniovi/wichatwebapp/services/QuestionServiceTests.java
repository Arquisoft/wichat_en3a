package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.repositories.QuestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTests {
    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    private Question testQuestion = new Question(new Answer("Paris", "en"), "Capital of France?", "flag.jpg");

    @Test
    public void getRandomQuestionTest() {
        when(questionRepository.getRandomQuestion(any())).thenReturn(testQuestion);

        Question question = questionService.getRandomQuestion(QuestionCategory.GEOGRAPHY);
        Assertions.assertEquals(testQuestion, question);
    }

    @Test
    public void removeQuestionTest() {
        testQuestion.setId("q123");
        questionService.removeQuestion(testQuestion);

        // Assert
        verify(questionRepository, times(1)).removeQuestion("q123");
    }
}
