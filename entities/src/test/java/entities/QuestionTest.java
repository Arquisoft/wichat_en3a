package entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class QuestionTest {
    private Question question;
    private Answer correctAnswer;
    private final String questionContent = "What is the capital of France?";
    private final String imageUrl = "france.jpg";

    @BeforeEach
    void setUp() {
        correctAnswer = new Answer("Paris", "en");
        question = new Question(correctAnswer, questionContent, imageUrl);
    }

    @Test
    void defaultConstructorTest() {
        Question emptyQuestion = new Question();

        Assertions.assertNull(emptyQuestion.getId());
        Assertions.assertNull(emptyQuestion.getContent());
        Assertions.assertNull(emptyQuestion.getImageUrl());
        Assertions.assertNull(emptyQuestion.getCorrectAnswer());
        Assertions.assertNull(emptyQuestion.getAnswers());
    }

    @Test
    void fullConstructorTest() {
        Assertions.assertEquals(questionContent, question.getContent());
        Assertions.assertEquals(imageUrl, question.getImageUrl());
        Assertions.assertEquals(correctAnswer, question.getCorrectAnswer());
        Assertions.assertEquals("en", question.getLanguage());
        Assertions.assertEquals(1, question.getAnswers().size());
        Assertions.assertTrue(question.getAnswers().contains(correctAnswer));
    }

    @Test
    void settersTest() {
        // Test all setters
        Answer newCorrectAnswer = new Answer("Madrid", "es");
        String newContent = "What is the capital of Spain?";
        String newImageUrl = "spain.jpg";
        String newId = "q123";
        List<Answer> newAnswers = new ArrayList<>();
        newAnswers.add(new Answer("Barcelona", "es"));
        newAnswers.add(new Answer("Valencia", "es"));

        question.setId(newId);
        question.setContent(newContent);
        question.setImageUrl(newImageUrl);
        question.setCorrectAnswer(newCorrectAnswer);
        question.setAnswers(newAnswers);

        Assertions.assertEquals(newId, question.getId());
        Assertions.assertEquals(newContent, question.getContent());
        Assertions.assertEquals(newImageUrl, question.getImageUrl());
        Assertions.assertEquals(newCorrectAnswer, question.getCorrectAnswer());
        Assertions.assertEquals(newAnswers, question.getAnswers());
        Assertions.assertEquals("es", question.getLanguage());
    }

    @Test
    void equalsHashMapTest(){
        Question question1 = new Question(correctAnswer, questionContent, imageUrl);
        Question question2 = new Question(correctAnswer, questionContent, imageUrl);

        Assertions.assertEquals(question1, question2);
        Assertions.assertEquals(question1.hashCode(), question2.hashCode());

        // question with different values
        Question question3 = new Question(new Answer("Berlin", "de"), "What is the capital of Germany?", "germany.jpg");
        Assertions.assertNotEquals(question1, question3);
        Assertions.assertNotEquals(question1.hashCode(), question3.hashCode());

    }
}
