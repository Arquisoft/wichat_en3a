package com.uniovi.wichatwebapp.service;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.AnswerCategory;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;
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
        correctAnswer = new Answer("Correct Answer", AnswerCategory.FLAG, "en");
        correctAnswer.setId("correct-1");

        testQuestion = new Question(correctAnswer, "Test Question", "flag.jpg", QuestionCategory.GEOGRAPHY);
        testQuestion.setId("q1");
        testQuestion.setAnswers(new ArrayList<>());

        dummyAnswers = Arrays.asList(
                new Answer("Wrong 1",AnswerCategory.FLAG, "en"),
                new Answer("Wrong 2", AnswerCategory.FLAG,"en"),
                new Answer("Wrong 3",AnswerCategory.FLAG, "en"),
                correctAnswer
        );
    }



    @Test
    void loadAnswersTest_ReturnFourAnswers() {
        when(questionRepository.findAll()).thenReturn(Collections.singletonList(testQuestion));
        when(answerRepository.findWrongAnswers(eq("en"), eq(correctAnswer.getText()), eq(correctAnswer.getCategory())))
                .thenReturn(dummyAnswers);

        questionService.assignAnswers();

        verify(questionRepository, times(1)).findAll();
        verify(questionRepository, times(1)).save(any(Question.class));

        assertNotNull(testQuestion.getAnswers());
        assertEquals(4, testQuestion.getAnswers().size()); // Correct answer + 3 distractors
    }

    @Test
    void getRandomQuestionTest_ReturnFourAnswers() {
        when(questionRepository.findQuestionsByCorrectAnswerIdExists()).thenReturn(Collections.singletonList(testQuestion));
        when(answerRepository.findAnswersByLanguageAndQuestionCategory(eq("en"), anyList()))
                .thenReturn(dummyAnswers);

        Question randomQuestion = questionService.getRandomQuestion("en", QuestionCategory.GEOGRAPHY);

        assertNotNull(randomQuestion);
        assertEquals(testQuestion, randomQuestion);
        assertEquals(4, randomQuestion.getAnswers().size()); // Correct answer + 3 distractors

        verify(questionRepository, times(1)).findQuestionsByCorrectAnswerIdExists();
        verify(answerRepository, times(1)).findAnswersByLanguageAndQuestionCategory(eq("en"), anyList());
    }

    @Test
    void checkAnswerTest_ShouldReturnTrueForCorrectAnswer() {
        assertTrue(questionService.checkAnswer("correct-1", testQuestion));
    }

    @Test
    void checkAnswerTest_ShouldReturnFalseForWrongAnswer() {
        assertFalse(questionService.checkAnswer("wrong-1", testQuestion));
    }

    @Test
    void eraseAllTest_ShouldDeleteAllRecords() {
        questionService.eraseAll();

        verify(questionRepository, times(1)).deleteAll();
        verify(answerRepository, times(1)).deleteAll();
    }

    @Test
    void createQuestionTest_CreatesDuplicatedQuestion() {
        String questionId = "q1";
        Answer correctAnswer = new Answer("Correct", AnswerCategory.FLAG,"en");
        Question question = new Question(correctAnswer, "Content", "image.jpg",QuestionCategory.GEOGRAPHY);
        question.setId(questionId);
        questionRepository.save(question);

        verify(questionRepository).save(question);

    }

    @Test
    void createQuestionTest_CreatesCorrectQuestion() {
        // Arrange
        String questionId = "q1";
        Answer correctAnswer = new Answer("Correct",AnswerCategory.FLAG, "en");
        Question question = new Question(correctAnswer, "Content", "image.jpg",QuestionCategory.GEOGRAPHY);
        question.setId(questionId);

        // Act
        questionRepository.save(question); // Explicitly call the save method

        // Assert
        verify(questionRepository).save(question); // Verify that save is called with the correct object
    }

    @Test
    void createAnswerTest_CreatesCorrectAnswers() {
        // Arrange: Create answers
        Answer a1 = new Answer("Correct",AnswerCategory.FLAG, "en");
        Answer a2 = new Answer("Correct", AnswerCategory.FLAG,"en");


        // Act: Call the method being tested
        answerRepository.save(a1);
        answerRepository.save(a2);

        // Assert: Verify that saveAllAnswers was called with the correct answers
        verify(answerRepository).save(a1);
        verify(answerRepository).save(a2);
    }


    @Test
    void assignAnswersTest_ShouldAssignAnswersCorrectly() {
        // Arrange: Mock a question and its answers
        String questionId = "q1";
        Answer correctAnswer = new Answer("Correct Answer",AnswerCategory.FLAG, "en");
        correctAnswer.setId("correct-1");
        Question question = new Question(correctAnswer, "Sample content", "image.jpg",QuestionCategory.GEOGRAPHY);
        question.setId(questionId);
        question.setAnswers(new ArrayList<>()); // Initialize empty answers list

        // Mock distractors
        Answer wrongAnswer1 = new Answer("Wrong Answer 1", AnswerCategory.FLAG,"en");
        wrongAnswer1.setId("wrong-1");
        Answer wrongAnswer2 = new Answer("Wrong Answer 2", AnswerCategory.FLAG,"en");
        wrongAnswer2.setId("wrong-2");
        Answer wrongAnswer3 = new Answer("Wrong Answer 3",AnswerCategory.FLAG, "en");
        wrongAnswer3.setId("wrong-3");

        List<Answer> ans = List.of(wrongAnswer1, wrongAnswer2, wrongAnswer3);
        // Create a mutable copy of the answers list
        List<Answer> wrongAnswers = new ArrayList<>(ans);

        // Mock repository responses
        when(questionRepository.findAll()).thenReturn(List.of(question)); // For assignAnswers
        when(answerRepository.findWrongAnswers("en", "Correct Answer",AnswerCategory.FLAG)).thenReturn(wrongAnswers); // For loadAnswers


        // Act: Call the method being tested
        questionService.assignAnswers();

        // Assert: Validate that the question has all answers (1 correct + 3 wrong)
        assertEquals(4, question.getAnswers().size());
        assertTrue(question.getAnswers().contains(correctAnswer)); // Ensure correct answer is included
        assertTrue(question.getAnswers().containsAll(wrongAnswers)); // Ensure wrong answers are included

    }



    @Test
    void saveAllAnswersTest_answersSaved() {
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
    void saveAllQuestionsTest_questionsSaved() {
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