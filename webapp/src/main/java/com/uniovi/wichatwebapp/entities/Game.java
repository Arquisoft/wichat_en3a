package com.uniovi.wichatwebapp.entities;

import com.uniovi.wichatwebapp.services.QuestionService;
import entities.Question;
import entities.QuestionCategory;

public class Game extends AbstractGame{

    public Game(QuestionCategory category) {
        setCategory(category);
    }

    public Game(QuestionCategory category, int timer, int maxNumberOfQuestions) {
        setCategory(category);
        setTimer(timer);
        setMaxNumberOfQuestions(maxNumberOfQuestions);
    }

    @Override
    public void nextQuestion(QuestionService questionService){
        Question question = questionService.getRandomQuestion(this.category);
        setCurrentQuestion(question);
        questionService.removeQuestion(question);
    }
}
