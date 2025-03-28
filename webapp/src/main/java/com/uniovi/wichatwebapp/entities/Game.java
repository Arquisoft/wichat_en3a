package com.uniovi.wichatwebapp.entities;

public class Game {
    private int maxNumberOfQuestions = 10;
    private int points;
    private int questions = 0;
    private int rightAnswers = 0;
    private int wrongAnswers = 0;
    private Question currentQuestion;
    private QuestionCategory category;
    private int timer = 30;

    public Game(QuestionCategory category) {
        this.category = category;
    }

    public Game(QuestionCategory category, int timer, int maxNumberOfQuestions) {
        this.category = category;
        this.timer = timer;
        this.maxNumberOfQuestions = maxNumberOfQuestions;
    }

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

    public QuestionCategory getCategory() {
        return category;
    }
}
