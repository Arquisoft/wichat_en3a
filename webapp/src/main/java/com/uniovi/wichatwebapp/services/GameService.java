package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.AbstractGame;
import com.uniovi.wichatwebapp.entities.Game;

import com.uniovi.wichatwebapp.entities.GameAllCategories;
import com.uniovi.wichatwebapp.entities.MultiPlayerGame;
import entities.Question;
import entities.QuestionCategory;
import entities.Score;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;


@Service
@SessionScope
public class GameService {

    private final QuestionService questionService;
    private AbstractGame game;
    public GameService(QuestionService questionService) {
        this.questionService = questionService;
    }
    private QuestionCategory category;
    public void correctAnswer(){
        game.correctAnswer();
    }
    public int getPoints() {
        return game.getPoints();
    }
    public void wrongAnswer(){
        game.wrongAnswer();
    }
    private boolean isMultiplayer = false;

    public void start(QuestionCategory category){
        game = new Game(category);
        this.category = category;
        nextQuestion();
    }

    public void start(QuestionCategory category, int timer, int questions){
        game = new Game(category, timer, questions);
        this.category = category;
        game.nextQuestion(questionService);
    }

    public void start(QuestionCategory category, Score score){
        game = new MultiPlayerGame(score.getQuestions(), category, score.getScore());
        this.isMultiplayer = true;
        this.category = category;
        game.setTimer(score.getQuestionTime());
        game.setMaxNumberOfQuestions(score.getQuestions().size() - 1);
        game.nextQuestion(questionService);
    }

    public void startAllCategoriesGame(){
        game = new GameAllCategories();
        this.category=null;
        game.nextQuestion(questionService);
    }

    public int getMultiPlayerScore(){
        if(isMultiplayer) {
            return ((MultiPlayerGame) game).getScore();
        }
        return 0;
    }


    public QuestionCategory getCategory(){
        return category;
    }

    public boolean hasGameEnded(){
        return game.hasGameEnded();
    }

    public void nextQuestion(){
        game.nextQuestion(questionService);
    }

    public Question getCurrentQuestion() {
        return game.getCurrentQuestion();
    }

    public void checkAnswer(String id){
        if(game.checkAnswer(id)){
            correctAnswer();
        }else{
            wrongAnswer();
        }
    }

    public int getRightAnswers() {
        return game.getRightAnswers();
    }

    public int getWrongAnswers() {
        return game.getWrongAnswers();
    }

    public int getTimer(){
        return game.getTimer();
    }

    public int getMaxQuestions(){
        return game.getMaxNumberOfQuestions();
    }

    public AbstractGame getGame() {
        return game;
    }

    public boolean isMultiplayer() {
        return isMultiplayer;
    }


}