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





}