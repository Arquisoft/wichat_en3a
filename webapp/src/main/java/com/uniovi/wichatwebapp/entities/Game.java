package com.uniovi.wichatwebapp.entities;

import com.uniovi.wichatwebapp.services.QuestionService;
import entities.Question;
import entities.QuestionCategory;

public class Game extends AbstractGame{
    private final QuestionCategory category;

    public Game(QuestionCategory category) {
        this.category = category;
    }

    public Game(QuestionCategory category, int timer, int maxNumberOfQuestions) {
        this.category = category;
        setTimer(timer);
        setMaxNumberOfQuestions(maxNumberOfQuestions);
    }

    public QuestionCategory getCategory() {
        return category;
    }

    @Override
    public void nextQuestion(QuestionService questionService){
        Question question = questionService.getRandomQuestion(this.category);
        setCurrentQuestion(question);
        questionService.removeQuestion(question);
    }
}
