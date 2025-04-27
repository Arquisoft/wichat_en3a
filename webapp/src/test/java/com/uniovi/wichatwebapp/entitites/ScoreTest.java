package com.uniovi.wichatwebapp.entitites;

import entities.Question;
import entities.Score;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Fail.fail;

@ExtendWith(MockitoExtension.class)
public class ScoreTest {

        @Test
        void defaultConstructorTest() {
            Score score = new Score();

            Assertions.assertNull(score.getId());
            Assertions.assertNull(score.getUser_id());
            Assertions.assertNull(score.getUser());
            Assertions.assertNull(score.getCategory());
            Assertions.assertEquals(0, score.getScore());
            Assertions.assertEquals(0, score.getRightAnswers());
            Assertions.assertEquals(0, score.getWrongAnswers());
        }

        @Test
        void fullConstructorTest() {
            Score score = new Score("user123", "testUser", "GEOGRAPHY", 100, 5, 2);

            Assertions.assertEquals("user123", score.getUser_id());
            Assertions.assertEquals("testUser", score.getUser());
            Assertions.assertEquals("GEOGRAPHY", score.getCategory());
            Assertions.assertEquals(100, score.getScore());
            Assertions.assertEquals(5, score.getRightAnswers());
            Assertions.assertEquals(2, score.getWrongAnswers());
            Assertions.assertNull(score.getId()); // ID not set in this constructor
        }

        @Test
        void noIdConstructorTest() {
            Score score = new Score("testUser", "HISTORY", 85, 4, 3);

            Assertions.assertEquals("noId", score.getUser_id());
            Assertions.assertEquals("testUser", score.getUser());
            Assertions.assertEquals("HISTORY", score.getCategory());
            Assertions.assertEquals(85, score.getScore());
            Assertions.assertEquals(4, score.getRightAnswers());
            Assertions.assertEquals(3, score.getWrongAnswers());
            Assertions.assertNull(score.getId());
        }

        @Test
        void setterAndGetterTest() {
            Score score = new Score();

            // Set all fields
            score.setId("score123");
            score.setUser_id("user456");
            score.setUser("newUser");
            score.setCategory("SCIENCE");
            score.setScore(200);
            score.setRightAnswers(10);
            score.setWrongAnswers(5);
            List<Question> questions = new ArrayList<>();
            score.setQuestions(questions);
            score.setQuestionTime(5);

            // Verify all fields
            Assertions.assertEquals("score123", score.getId());
            Assertions.assertEquals("user456", score.getUser_id());
            Assertions.assertEquals("newUser", score.getUser());
            Assertions.assertEquals("SCIENCE", score.getCategory());
            Assertions.assertEquals(200, score.getScore());
            Assertions.assertEquals(10, score.getRightAnswers());
            Assertions.assertEquals(5, score.getWrongAnswers());
            Assertions.assertEquals(questions, score.getQuestions());
            Assertions.assertEquals(5, score.getQuestionTime());
        }


    }
