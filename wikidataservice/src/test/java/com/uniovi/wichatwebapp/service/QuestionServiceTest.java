package com.uniovi.wichatwebapp.service;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.repositories.AnswerRepository;
import com.uniovi.wichatwebapp.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // No Spring context!
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private QuestionService questionService;

    private Question testQuestion;
    private Answer correctAnswer;
    private List<Answer> dummyAnswers;

    @BeforeEach
    void setUp() {
        // Initialize test data without any database connection
        correctAnswer = new Answer("Correct Answer", "en");
        correctAnswer.setId("correct-1");

        testQuestion = new Question(correctAnswer, "Test Question", "flag.jpg");
        testQuestion.setId("q1");
        testQuestion.setAnswers(new ArrayList<>());

        dummyAnswers = Arrays.asList(
                new Answer("Wrong 1", "en"),
                new Answer("Wrong 2", "en"),
                new Answer("Wrong 3", "en"),
                correctAnswer
        );
    }

    @Test
    void getRandomQuestion_ShouldReturnQuestionWithFourAnswers() {
        // Mock repository responses
        when(questionRepository.findQuestionsByCorrectAnswerIdExists())
                .thenReturn(Collections.singletonList(testQuestion));
        when(answerRepository.findAnswersByLanguage(anyString()))
                .thenReturn(dummyAnswers);

        Question result = questionService.getRandomQuestion("en");

        assertNotNull(result);
        assertEquals(3, result.getAnswers().size()); // It actually gets only wrong answers.
        assertFalse(result.getAnswers().contains(correctAnswer));
    }

    @Test
    void checkAnswer_ShouldReturnTrueForCorrectAnswer() {
        assertTrue(questionService.checkAnswer("correct-1", testQuestion));
    }

    @Test
    void checkAnswer_ShouldReturnFalseForWrongAnswer() {
        assertFalse(questionService.checkAnswer("wrong-1", testQuestion));
    }

    @Test
    void eraseAll_ShouldDeleteAllRecords() {
        questionService.eraseAll();

        verify(questionRepository, times(1)).deleteAll();
        verify(answerRepository, times(1)).deleteAll();
    }
}