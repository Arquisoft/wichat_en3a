package com.uniovi.wikidataservice.entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.*;
import java.util.List;

@Document(
        collection = "answers"
)
public class Answer{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
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

    public String getId() {
        return id;
    }
}
