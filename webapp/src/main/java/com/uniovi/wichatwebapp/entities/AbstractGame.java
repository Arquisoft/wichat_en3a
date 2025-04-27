package com.uniovi.wichatwebapp.entities;

import java.util.ArrayList;
import java.util.List;

import com.uniovi.wichatwebapp.services.QuestionService;
import entities.Question;
import entities.QuestionCategory;

public abstract class AbstractGame {
    private int maxNumberOfQuestions = 10;
    private int points;
    private int questions = 0;
    private int rightAnswers = 0;
    private int wrongAnswers = 0;
    private Question currentQuestion;
    private int timer = 30;
    private List<Question> questionList = new ArrayList<>();
    private QuestionCategory category;


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
        questionList.add(currentQuestion);
        this.currentQuestion = currentQuestion;
    }

    public boolean hasQuestion(Question question) {
        return questionList.contains(question);
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

    public boolean isQuestionInGame(Question question) {
        return questionList.contains(question);
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public QuestionCategory getCategory() {
        return category;
    }

    public void setCategory(QuestionCategory category) {
        this.category = category;
    }

    public abstract void nextQuestion(QuestionService questionService);
}
