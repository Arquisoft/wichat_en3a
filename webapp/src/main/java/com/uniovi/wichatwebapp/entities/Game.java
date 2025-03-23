package com.uniovi.wichatwebapp.entities;

public class Game {

    private int points;
    private int questions = 0;
    private int rightAnswers = 0;
    private int wrongAnswers = 0;
    private Question currentQuestion;

    public Game() {

    }

    public boolean checkAnswer(String id) {
        return currentQuestion.getCorrectAnswer().getId().equals(id);
    }

    public boolean hasGameEnded() {
        return questions >= 10;
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

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }
}
