package com.uniovi.wichatwebapp.services.questionstrategies;

import com.uniovi.wichatwebapp.entities.Game;
import com.uniovi.wichatwebapp.services.QuestionService;
import entities.Question;

public class RandomQuestionStrategy implements GameQuestionStrategy{
    private Game game;
    private QuestionService questionService;

    public RandomQuestionStrategy(Game game, QuestionService questionService) {
        this.game = game;
        this.questionService = questionService;
    }

    @Override
    public void nextQuestion() {
        Question question = questionService.getRandomQuestion(game.getCategory());
        int attempts = 0;
        while(game.isQuestionInGame(question) && attempts < 10) {
            question = questionService.getRandomQuestion(game.getCategory());
            attempts++;
        }
        game.setCurrentQuestion(question);
    }
}
