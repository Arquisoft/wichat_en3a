package com.uniovi.wichatwebapp.entities;


import java.util.List;


public class Answer{
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String text;
    private String language;
    //@OneToMany(mappedBy = "correctAnswer", fetch = FetchType.EAGER)
    private List<Question> questions;
    //@ManyToMany(mappedBy = "answers", fetch = FetchType.EAGER)
    private List<Question> questionsWithThisAnswer;

    public Answer(String text, String language) {
        this.text = text;
        this.language = language;
    }

    public Answer() {
    }

    public String getLanguage() {
        return language;
    }

    public String getText() {
        return text;
    }


}
