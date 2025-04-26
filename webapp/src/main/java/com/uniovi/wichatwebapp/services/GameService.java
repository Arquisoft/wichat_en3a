package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.Game;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class GameService {

    private final QuestionService questionService;
    private Game game;

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
        nextQuestion();
    }

    public void start(QuestionCategory category, int timer, int questions){
        game = new Game(category, timer, questions);
        nextQuestion();
    }

    public QuestionCategory getCategory(){
        return game.getCategory();
    }

    public boolean hasGameEnded(){
        return game.hasGameEnded();
    }

    public void nextQuestion(){
        Question question = questionService.getRandomQuestion(game.getCategory());
        game.setCurrentQuestion(question);
        questionService.removeQuestion(question);
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
