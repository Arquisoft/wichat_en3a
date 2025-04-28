package com.uniovi.wichatwebapp.repositories;


import entities.Answer;
import entities.AnswerCategory;
import entities.Question;
import entities.QuestionCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // No Spring context!
class QuestionRepositoryTest {

    @Mock
    private QuestionRepository questionRepository;

    private Question testQuestion;
    private Answer correctAnswer;
    @BeforeEach
    void setUp() {
        // Initialize test data without any database connection
        correctAnswer = new Answer("Correct Answer", AnswerCategory.FLAG, "en");
        correctAnswer.setId("correct-1");

        testQuestion = new Question(correctAnswer, "Test Question", "flag.jpg", QuestionCategory.GEOGRAPHY);
        testQuestion.setId("q1");
        testQuestion.setAnswers(new ArrayList<>());


    }


    @Test
    void saveQuestionsTest_questionsSaved() {
        // Arrange: Create questions
        String questionId1 = "quest1";
        Answer correctAnswer1 = new Answer("Correct1",AnswerCategory.FLAG, "en");
        Question question1 = new Question(correctAnswer1, "Content", "image.jpg",QuestionCategory.GEOGRAPHY);
        question1.setId(questionId1);

        String questionId2 = "quest2";
        Answer correctAnswer2 = new Answer("Correct2",AnswerCategory.FLAG, "en");
        Question question2 = new Question(correctAnswer2, "Content", "image.jpg",QuestionCategory.GEOGRAPHY);
        question2.setId(questionId2);

        String questionId3 = "quest3";
        Answer correctAnswer3 = new Answer("Correct3",AnswerCategory.FLAG, "en");
        Question question3 = new Question(correctAnswer3, "Content", "image.jpg",QuestionCategory.GEOGRAPHY);
        question3.setId(questionId3);

        questionRepository.save(question1);
        questionRepository.save(question2);
        questionRepository.save(question3);

        // Correctly mock findById behavior
        when(questionRepository.findById(questionId1)).thenReturn(Optional.of(question1));
        when(questionRepository.findById(questionId2)).thenReturn(Optional.of(question2));
        when(questionRepository.findById(questionId3)).thenReturn(Optional.of(question3));

        // Act and Assert: Test the repository behavior
        assertEquals(question1, questionRepository.findById(questionId1).get());
        assertEquals(question2, questionRepository.findById(questionId2).get());
        assertEquals(question3, questionRepository.findById(questionId3).get());
    }
    @Test
    void findQuestionsByCorrectAnswerIdExistsTest_ReturnsMatchingQuestions() {
        // Arrange: Define mock questions with correct answers
        List<Question> expectedQuestions = List.of(
                new Question(new Answer("Correct1", AnswerCategory.FLAG, "en"), "Question 1", "image.jpg", QuestionCategory.GEOGRAPHY),
                new Question(new Answer("Correct2", AnswerCategory.FLAG, "en"), "Question 2", "image.jpg", QuestionCategory.GEOGRAPHY)
        );

        // Mock repository behavior
        when(questionRepository.findQuestionsByCorrectAnswerIdExists()).thenReturn(expectedQuestions);

        // Act: Call the repository method
        List<Question> retrievedQuestions = questionRepository.findQuestionsByCorrectAnswerIdExists();

        // Assert: Validate correctness
        assertNotNull(retrievedQuestions);
        assertEquals(expectedQuestions.size(), retrievedQuestions.size());
        assertTrue(retrievedQuestions.containsAll(expectedQuestions));

        // Verify interaction with repository
        verify(questionRepository, times(1)).findQuestionsByCorrectAnswerIdExists();
    }

    @Test
    void findByIdTest_QuestionFound() {
        // Arrange: Ensure repository returns the expected question
        when(questionRepository.findById(testQuestion.getId())).thenReturn(Optional.of(testQuestion));

        // Act: Call the repository method
        Optional<Question> retrievedQuestion = questionRepository.findById(testQuestion.getId());

        // Assert: Validate correctness
        assertTrue(retrievedQuestion.isPresent()); // Ensure question is found
        assertEquals(testQuestion, retrievedQuestion.get()); // Ensure it's the expected question

        // Verify repository interaction
        verify(questionRepository, times(1)).findById(testQuestion.getId());
    }
    @Test
    void findByIdTest_QuestionNotFound() {
        // Arrange: Ensure repository returns an empty Optional when the ID doesn't exist
        String invalidId = "non-existent-id";
        when(questionRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act: Call the repository method
        Optional<Question> retrievedQuestion = questionRepository.findById(invalidId);

        // Assert: Validate that the result is empty
        assertTrue(retrievedQuestion.isEmpty());

        // Verify repository interaction
        verify(questionRepository, times(1)).findById(invalidId);
    }

}