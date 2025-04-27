package com.uniovi.wichatwebapp.entitites;

import com.uniovi.wichatwebapp.entities.MultiPlayerGame;
import entities.Question;
import entities.QuestionCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class MultiPlayerGameTest {

    @Test
    void testCreation() {
        MultiPlayerGame game = new MultiPlayerGame(null, QuestionCategory.GEOGRAPHY, 20);
        Assertions.assertEquals(QuestionCategory.GEOGRAPHY, game.getCategory());
        Assertions.assertEquals(20, game.getScore());
    }

    @Test
    void testNextQuestion(){
        Question question1 = new Question();
        Question question2 = new Question();
        Question question3 = new Question();
        List<Question> questions = List.of(question1, question2, question3);
        MultiPlayerGame game = new MultiPlayerGame(questions, QuestionCategory.GEOGRAPHY, 20);
        game.nextQuestion(null);
        Assertions.assertEquals(question1, game.getCurrentQuestion());
        game.nextQuestion(null);
        Assertions.assertEquals(question2, game.getCurrentQuestion());
        game.nextQuestion(null);
        Assertions.assertEquals(question3, game.getCurrentQuestion());
    }
}
