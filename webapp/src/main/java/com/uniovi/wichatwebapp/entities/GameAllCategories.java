package com.uniovi.wichatwebapp.entities;

import com.uniovi.wichatwebapp.services.QuestionService;
import entities.Question;

public class GameAllCategories extends AbstractGame{


    @Override
    public void nextQuestion(QuestionService questionService) {
        Question question = questionService.getRandomQuestionNoCategory();
        setCurrentQuestion(question);
        questionService.removeQuestion(question);
    }
}
