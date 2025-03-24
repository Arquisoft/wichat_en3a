package com.uniovi.wichatwebapp.entities;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(
        collection = "answers"
)
public class Answer{
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String text;
    private String language;
    //@OneToMany(mappedBy = "correctAnswer", fetch = FetchType.EAGER)
    private List<Question> questions;

    //@ManyToMany(mappedBy = "answers", fetch = FetchType.EAGER)
    private List<Question> questionsWithThisAnswer;

    public Answer(String text, String language) {
        this.id = new ObjectId().toString(); // Generate a new ObjectId for the id field
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
