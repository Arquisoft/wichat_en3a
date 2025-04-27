package com.uniovi.wichatwebapp.entities;

import com.uniovi.wichatwebapp.services.QuestionService;
import entities.Question;
import entities.QuestionCategory;

import java.util.List;

public class MultiPlayerGame extends AbstractGame{
    private List<Question> questions;
    private int currentQuestion = 0;
    private int score;
    public MultiPlayerGame(List<Question> questions, QuestionCategory category, int score) {
        this.questions = questions;
        setCategory(category);
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void nextQuestion(QuestionService questionService) {
        setCurrentQuestion(questions.get(currentQuestion));
        currentQuestion++;
    }
}
