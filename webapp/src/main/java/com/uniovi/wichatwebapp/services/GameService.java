package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.AbstractGame;
import com.uniovi.wichatwebapp.entities.Game;

import com.uniovi.wichatwebapp.entities.GameAllCategories;
import entities.Question;
import entities.QuestionCategory;
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

    public void start(QuestionCategory category){
        game = new Game(category);
        this.category = category;
        nextQuestion();
    }

    public void start(QuestionCategory category, int timer, int questions){
        game = new Game(category, timer, questions);
        game.nextQuestion(questionService);
    }

    public void startAllCategoriesGame(){
        game = new GameAllCategories();
        this.category=null;
        game.nextQuestion(questionService);
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


}
