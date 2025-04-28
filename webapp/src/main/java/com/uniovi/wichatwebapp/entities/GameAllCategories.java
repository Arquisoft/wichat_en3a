package com.uniovi.wichatwebapp.entities;

import com.uniovi.wichatwebapp.services.QuestionService;
import entities.Question;

public class GameAllCategories extends AbstractGame{


    @Override
    public void nextQuestion(QuestionService questionService) {
        Question question = questionService.getRandomQuestionNoCategory();
        int tries = 0;
        while(isQuestionInGame(question) &&  tries < 10){
            question = questionService.getRandomQuestionNoCategory();
            tries++;
        }
        setCurrentQuestion(question);
    }
}
