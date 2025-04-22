package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.Game;

import com.uniovi.wichatwebapp.services.questionstrategies.GameQuestionStrategy;
import com.uniovi.wichatwebapp.services.questionstrategies.PredefinedQuestionsStrategy;
import com.uniovi.wichatwebapp.services.questionstrategies.RandomQuestionStrategy;
import entities.Question;
import entities.QuestionCategory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;

@Service
@SessionScope
public class GameService {

    private final QuestionService questionService;
    private Game game;
    private GameQuestionStrategy gameQuestionStrategy;

    public GameService(QuestionService questionService) {
        this.questionService = questionService;
    }

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
        gameQuestionStrategy = new RandomQuestionStrategy(game, questionService);
        nextQuestion();
    }

    public void start(QuestionCategory category, int timer, int questions){
        game = new Game(category, timer, questions);
        gameQuestionStrategy = new RandomQuestionStrategy(game, questionService);
        nextQuestion();
    }

    public void start(List<Question> questions, QuestionCategory category){
        game = new Game(category);
        gameQuestionStrategy = new PredefinedQuestionsStrategy(game, questions);
        nextQuestion();
    }

    public QuestionCategory getCategory(){
        return game.getCategory();
    }

    public boolean hasGameEnded(){
        return game.hasGameEnded();
    }

    public void nextQuestion(){
        gameQuestionStrategy.nextQuestion();
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

    public Game getGame() {
        return game;
    }
}
