package com.uniovi.wichatwebapp.repositories;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.AnswerCategory;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // No Spring context!
class AnswerRepositoryTest {

    @Mock
    private AnswerRepository answerRepository;



    @Test
    void saveAnswersTest_answersSaved() {
        // Arrange: Create answers
        Answer a1 = new Answer("Correct",AnswerCategory.FLAG, "en");
        Answer a2 = new Answer("Incorrect", AnswerCategory.FLAG,"en");
        Answer a3 = new Answer("Maybe",AnswerCategory.FLAG, "en");


        // Act: Call the method being tested
        answerRepository.save(a1);
        answerRepository.save(a2);
        answerRepository.save(a3);

        // Assert: Verify that saveAllAnswers was called with the correct answers
        verify(answerRepository).save(a1);
        verify(answerRepository).save(a2);
        verify(answerRepository).save(a3);

    }


    @Test
    void findWrongAnswersTest_ReturnsCorrectAnswers() {
        // Arrange: Define test parameters
        String language = "en";
        String correctAnswerText = "Correct Answer";
        AnswerCategory category = AnswerCategory.FLAG;

        // Define mock wrong answers
        List<Answer> wrongAnswers = List.of(
                new Answer("Wrong 1", category, language),
                new Answer("Wrong 2", category, language),
                new Answer("Wrong 3", category, language)
        );

        // Mock repository behavior
        when(answerRepository.findWrongAnswers(language, correctAnswerText, category)).thenReturn(wrongAnswers);

        // Act: Call the repository method
        List<Answer> retrievedAnswers = answerRepository.findWrongAnswers(language, correctAnswerText, category);

        // Assert: Validate correctness
        assertNotNull(retrievedAnswers);
        assertEquals(3, retrievedAnswers.size());
        assertTrue(retrievedAnswers.containsAll(wrongAnswers));

        // Verify interaction with repository
        verify(answerRepository, times(1)).findWrongAnswers(language, correctAnswerText, category);
    }

    @Test
    void findAnswersByLanguageAndQuestionCategoryTest_ReturnsMatchingAnswers() {
        // Arrange: Define test parameters
        String language = "en";
        List<AnswerCategory> categories = List.of(AnswerCategory.FLAG, AnswerCategory.MONUMENT_NAME);

        // Define mock answers that match the query
        List<Answer> expectedAnswers = List.of(
                new Answer("Answer 1", AnswerCategory.FLAG, language),
                new Answer("Answer 2", AnswerCategory.MONUMENT_NAME, language)
        );

        // Mock repository behavior
        when(answerRepository.findAnswersByLanguageAndQuestionCategory(language, categories)).thenReturn(expectedAnswers);

        // Act: Call the repository method
        List<Answer> retrievedAnswers = answerRepository.findAnswersByLanguageAndQuestionCategory(language, categories);

        // Assert: Validate correctness
        assertNotNull(retrievedAnswers);
        assertEquals(expectedAnswers.size(), retrievedAnswers.size());
        assertTrue(retrievedAnswers.containsAll(expectedAnswers));

        // Verify interaction with repository
        verify(answerRepository, times(1)).findAnswersByLanguageAndQuestionCategory(language, categories);
    }

    @Test
    void findAnswersByLanguageTest_ReturnsMatchingAnswers() {
        // Arrange: Define test parameters
        String language = "en";

        // Define mock answers that match the query
        List<Answer> expectedAnswers = List.of(
                new Answer("Answer 1", AnswerCategory.FLAG, language),
                new Answer("Answer 2", AnswerCategory.BRAND, language),
                new Answer("Answer 3", AnswerCategory.MOVIE_YEAR, language)
        );

        // Mock repository behavior
        when(answerRepository.findAnswersByLanguage(language)).thenReturn(expectedAnswers);

        // Act: Call the repository method
        List<Answer> retrievedAnswers = answerRepository.findAnswersByLanguage(language);

        // Assert: Validate correctness
        assertNotNull(retrievedAnswers);
        assertEquals(expectedAnswers.size(), retrievedAnswers.size());
        assertTrue(retrievedAnswers.containsAll(expectedAnswers));

        // Verify interaction with repository
        verify(answerRepository, times(1)).findAnswersByLanguage(language);
    }

}