package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.Question;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final QuestionService questionService;

    private int points;
    private int questions = 0;
    private int rightAnswers = 0;
    private int wrongAnswers = 0;
    private Question currentQuestion;

    public GameService(QuestionService questionService) {
        this.questionService = questionService;
    }

    public void correctAnswer(){
        rightAnswers++;
        points+=100;
        questions++;
    }
    public int getPoints() {
        return points;
    }
    public void wrongAnswer(){
        wrongAnswers++;
        points-=25;
        questions++;
    }
    public void start(){
        questions=0;
        points=0;
        //nextQuestion();
    }

    public boolean hasGameEnded(){
        return questions>=10;
    }

    public void nextQuestion(){
        currentQuestion = questionService.getRandomQuestion();
        questionService.removeQuestion(currentQuestion);
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void checkAnswer(String id){
        if(currentQuestion.getCorrectAnswer().getId().equals(id)){
            correctAnswer();
        }else{
            wrongAnswer();
        }
    }

    public int getRightAnswers() {
        return rightAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }
}
