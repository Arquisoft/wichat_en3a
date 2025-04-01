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
class QuestionRepositoryTest {

    @Mock
    private QuestionRepository questionRepository;



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


}