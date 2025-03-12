package com.uniovi.wichatwebapp.services;

import org.springframework.stereotype.Service;

@Service
public class GameService {
    private int points;
    private int questions = 0;
    public void correctAnswer(){
        points+=100;
        questions++;
    }
    public int getPoints() {
        return points;
    }
    public void wrongAnswer(){
        points-=25;
        questions++;
    }
    public void newGameCheck(){
        questions=0;
        points=0;
    }
    public int getQuestions() {
        return questions;
    }
}
