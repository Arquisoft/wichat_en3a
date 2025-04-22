package com.uniovi.wichatwebapp.services.questionstrategies;

import com.uniovi.wichatwebapp.entities.Game;
import entities.Question;

import java.util.List;

public class PredefinedQuestionsStrategy implements GameQuestionStrategy{
    private Game game;
    private List<Question> questions;
    private int currentQuestionIndex = 0;

    public PredefinedQuestionsStrategy(Game game, List<Question> questions) {
        this.game = game;
        this.questions = questions;
    }

    @Override
    public void nextQuestion() {
        game.setCurrentQuestion(questions.get(currentQuestionIndex));
        currentQuestionIndex++;
    }
}
