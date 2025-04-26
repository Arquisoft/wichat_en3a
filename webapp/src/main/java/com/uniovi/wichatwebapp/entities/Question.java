package com.uniovi.wichatwebapp.entities;

import java.util.ArrayList;
import java.util.List;


public class Question {

        private String id;

        private List<Answer> answers;

        private Answer correctAnswer;
        private String content;
        private String imageUrl;

    public Question() {
    }

    public Question(Answer correctAnswer, String content, String imageUrl) {
        this.correctAnswer = correctAnswer;
        this.content = content;
        this.imageUrl = imageUrl;
        this.answers = new ArrayList<>();
        this.answers.add(correctAnswer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public String getContent() {
        return content;
    }

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }
    public String getLanguage(){
        return correctAnswer.getLanguage();
    }

    public void setAnswers(List<Answer> answers) {
        this.answers=answers;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
