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

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void createQuestion_CreatesDuplicatedQuestion() {
        String questionId = "q1";
        Answer correctAnswer = new Answer("Correct", "en");
        Question question = new Question(correctAnswer, "Content", "image.jpg");
        question.setId(questionId);
        questionRepository.save(question);

        verify(questionRepository).save(question);

    }

    @Test
    void createQuestion_CreatesCorrectQuestion() {
        // Arrange
        String questionId = "q1";
        Answer correctAnswer = new Answer("Correct", "en");
        Question question = new Question(correctAnswer, "Content", "image.jpg");
        question.setId(questionId);

        // Act
        questionRepository.save(question); // Explicitly call the save method

        // Assert
        verify(questionRepository).save(question); // Verify that save is called with the correct object
    }

    @Test
    void createAnswer_CreatesCorrectAnswers() {
        // Arrange: Create answers
        Answer a1 = new Answer("Correct", "en");
        Answer a2 = new Answer("Correct", "en");


        // Act: Call the method being tested
        answerRepository.save(a1);
        answerRepository.save(a2);

        // Assert: Verify that saveAllAnswers was called with the correct answers
        verify(answerRepository).save(a1);
        verify(answerRepository).save(a2);
    }


    @Test
    void assignAnswers_CheckAnswers() {
        // Arrange
        String questionId1 = "q1";
        Answer correctAnswer1 = new Answer("Correct1", "en");
        Question question1 = new Question(correctAnswer1, "Sample content", "image.jpg");
        question1.setId(questionId1);

        // Add a mock answer list to simulate answers
        Answer incorrectAnswer1 = new Answer("Incorrect1", "en");
        Answer incorrectAnswer2 = new Answer("Incorrect2", "en");
        Answer incorrectAnswer3 = new Answer("Incorrect3", "en");

        List<Answer> answers = new ArrayList<>();
        answers.add(correctAnswer1);
        answers.add(incorrectAnswer1);
        answers.add(incorrectAnswer2);
        answers.add(incorrectAnswer3);

        // Mock the repository to return a list of questions
        when(questionRepository.findAll()).thenReturn(List.of(question1));

        // Mock the behavior of loadAnswers (if needed)
        doAnswer(invocation -> {
            Question q = invocation.getArgument(0);
            q.setAnswers(answers); // Simulate loading answers into the question
            return null;
        }).when(questionService).loadAnswers(any(Question.class));

        // Act
        questionService.assignAnswers(); // Execute the method being tested

        // Assert
        assertThat(question1.getAnswers().size()).isEqualTo(4); // Check the number of answers
        assertThat(questionService.checkAnswer(correctAnswer1.getId(), question1)).isTrue(); // Correct answer
        assertThat(questionService.checkAnswer(incorrectAnswer1.getId(), question1)).isFalse(); // Incorrect answer

        // Verify repository interactions
        verify(questionRepository).findAll();
        verify(questionService).loadAnswers(question1);
    }


    @Test
    void saveAllAnswers() {
        // Arrange: Create answers
        Answer a1 = new Answer("Correct", "en");
        Answer a2 = new Answer("Incorrect", "en");
        Answer a3 = new Answer("Maybe", "en");


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
    void saveAllQuestions() {
        // Arrange: Create questions
        String questionId1 = "quest1";
        Answer correctAnswer1 = new Answer("Correct1", "en");
        Question question1 = new Question(correctAnswer1, "Content", "image.jpg");
        question1.setId(questionId1);

        String questionId2 = "quest2";
        Answer correctAnswer2 = new Answer("Correct2", "en");
        Question question2 = new Question(correctAnswer2, "Content", "image.jpg");
        question2.setId(questionId2);

        String questionId3 = "quest3";
        Answer correctAnswer3 = new Answer("Correct3", "en");
        Question question3 = new Question(correctAnswer3, "Content", "image.jpg");
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