package com.uniovi.wichatwebapp.entities;

import com.uniovi.wichatwebapp.services.QuestionService;
import entities.Question;

public abstract class AbstractGame {
    private int maxNumberOfQuestions = 10;
    private int points;
    private int questions = 0;
    private int rightAnswers = 0;
    private int wrongAnswers = 0;
    private Question currentQuestion;
    private int timer = 30;


    public boolean checkAnswer(String id) {
        return currentQuestion.getCorrectAnswer().getId().equals(id);
    }

    public boolean hasGameEnded() {
        return questions >= maxNumberOfQuestions;
    }

    public void correctAnswer(){
        rightAnswers++;
        points+=100;
        questions++;
    }

    public void wrongAnswer(){
        wrongAnswers++;
        points-=25;
        questions++;
    }

    public int getPoints() {
        return points;
    }

    public int getQuestions() {
        return questions;
    }

    public int getRightAnswers() {
        return rightAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public int getMaxNumberOfQuestions() {
        return maxNumberOfQuestions;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public void setMaxNumberOfQuestions(int maxNumberOfQuestions) {
        this.maxNumberOfQuestions = maxNumberOfQuestions;
    }
    public abstract void nextQuestion(QuestionService questionService);
}
