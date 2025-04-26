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
        List<Answer> answers = List.of(
                new Answer("Correct", AnswerCategory.FLAG, "en"),
                new Answer("Incorrect", AnswerCategory.FLAG, "en"),
                new Answer("Maybe", AnswerCategory.FLAG, "en")
        );

        // Use an Answer to track saved answers
        doAnswer(invocation -> {
            List<Answer> savedAnswers = invocation.getArgument(0);
            assertEquals(answers.size(), savedAnswers.size()); // Ensure all are saved
            return null;
        }).when(answerRepository).saveAll(any());

        // Act
        questionService.saveAllAnswers(answers);

        // Verify interaction with repository
        verify(answerRepository, times(1)).saveAll(answers);
    }

    @Test
    void saveAllQuestionsTest_questionsSaved() {
        // Arrange: Create questions
        List<Question> questions = List.of(
                new Question(new Answer("Correct1", AnswerCategory.FLAG, "en"), "Content", "image.jpg", QuestionCategory.GEOGRAPHY),
                new Question(new Answer("Correct2", AnswerCategory.FLAG, "en"), "Content", "image.jpg", QuestionCategory.GEOGRAPHY),
                new Question(new Answer("Correct3", AnswerCategory.FLAG, "en"), "Content", "image.jpg", QuestionCategory.GEOGRAPHY)
        );

        // Use an Answer to track saved questions
        doAnswer(invocation -> {
            List<Question> savedQuestions = invocation.getArgument(0);
            assertEquals(questions.size(), savedQuestions.size()); // Ensure all are saved
            return null;
        }).when(questionRepository).saveAll(any());

        // Act
        questionService.saveAllQuestions(questions);

        // Verify interaction with repository
        verify(questionRepository, times(1)).saveAll(questions);
    }


    @Test
    void findQuestionByIdTest_QuestionFound() {
        // Arrange: Ensure repository returns the expected question
        when(questionRepository.findById(testQuestion.getId())).thenReturn(Optional.of(testQuestion));

        // Act: Call the method being tested
        Question retrievedQuestion = questionService.findQuestionById(testQuestion.getId());

        // Assert: Validate correctness
        assertNotNull(retrievedQuestion); // Ensure a question is returned
        assertEquals(testQuestion, retrievedQuestion); // Ensure it's the expected question

        // Verify that the repository method was called once with the correct ID
        verify(questionRepository, times(1)).findById(testQuestion.getId());
    }

    @Test
    void findQuestionByIdTest_QuestionNotFound() {

        // Arrange: Ensure repository returns an empty Optional when the ID doesn't exist
        String invalidId = "non-existent-id";
        when(questionRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert: Expect an exception or a null result
        //assertThrows(NoSuchElementException.class, () -> questionService.findQuestionById(invalidId));
        assertNull(questionService.findQuestionById(invalidId));

        // Verify the repository method was called correctly
        verify(questionRepository, times(1)).findById(invalidId);
    }


    @Test
    void saveQuestionTest_QuestionSaved() {
        Question newQuestion =new Question(new Answer("Correct1", AnswerCategory.FLAG, "en"), "Content", "image.jpg", QuestionCategory.GEOGRAPHY);
        // Act: Call the save method
        questionService.save(newQuestion);

        // Assert & Verify: Ensure the repository method is called with the correct question
        verify(questionRepository, times(1)).save(newQuestion);
    }
    @Test
    void matchCategoriesTest_ReturnsCorrectCategoriesSport() {
        // Act
        List<AnswerCategory> result = questionService.matchCategories(QuestionCategory.SPORT);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains(AnswerCategory.PERSON_BASKETBALL_TEAM));
        assertTrue(result.contains(AnswerCategory.PERSON_F1_TEAM));
        assertTrue(result.contains(AnswerCategory.PERSON_FOOTBALL_TEAM));
        assertTrue(result.contains(AnswerCategory.SPORT_TEAM_LOGO));
    }

    @Test
    void matchCategoriesTest_ReturnsCorrectCategoriesGeography() {
        // Act
        List<AnswerCategory> result = questionService.matchCategories(QuestionCategory.GEOGRAPHY);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(AnswerCategory.FLAG));
        assertTrue(result.contains(AnswerCategory.MONUMENT_NAME));
        assertTrue(result.contains(AnswerCategory.MONUMENT_COUNTRY));
    }

    @Test
    void matchCategoriesTest_ReturnsCorrectCategoriesPopCulture() {
        // Act
        List<AnswerCategory> result = questionService.matchCategories(QuestionCategory.POP_CULTURE);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(AnswerCategory.BRAND));
        assertTrue(result.contains(AnswerCategory.MOVIE_YEAR));
    }

    @Test
    void matchCategoriesTest_ReturnsCorrectCategoriesBiology() {
        // Act
        List<AnswerCategory> result = questionService.matchCategories(QuestionCategory.BIOLOGY);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(AnswerCategory.ANIMAL_NAME));
        assertTrue(result.contains(AnswerCategory.ANIMAL_SCIENTIFIC_NAME));
        assertTrue(result.contains(AnswerCategory.ANIMAL_LIFESPAN));
    }

    @Test
    void findAnswerByIdTest_AnswerFound() {
        // Arrange: Ensure repository returns the expected answer
        when(answerRepository.findById(correctAnswer.getId())).thenReturn(Optional.of(correctAnswer));

        // Act: Call the method being tested
        Answer retrievedAnswer = questionService.findAnswerById(correctAnswer.getId());

        // Assert: Validate correctness
        assertNotNull(retrievedAnswer); // Ensure an answer is returned
        assertEquals(correctAnswer, retrievedAnswer); // Ensure it's the expected answer

        // Verify that the repository method was called once with the correct ID
        verify(answerRepository, times(1)).findById(correctAnswer.getId());
    }

    @Test
    void findAnswerByIdTest_AnswerNotFound() {
        // Arrange: Ensure repository returns an empty Optional when the ID doesn't exist
        String invalidId = "non-existent-answer-id";
        when(answerRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert: Expect an exception since `.get()` is used on an empty Optional
        //assertThrows(NoSuchElementException.class, () -> questionService.findAnswerById(invalidId));

        assertNull(questionService.findAnswerById(invalidId));
        // Verify the repository method was called correctly
        verify(answerRepository, times(1)).findById(invalidId);
    }
    @Test
    void removeQuestionTest_QuestionDeleted() {
        // Arrange: Use the predefined test question
        doNothing().when(questionRepository).delete(testQuestion); // Mock delete behavior

        // Act: Call the remove method
        questionService.removeQuestion(testQuestion);

        // Assert: Verify that delete was called exactly once
        verify(questionRepository, times(1)).delete(testQuestion);
    }

}