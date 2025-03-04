package com.uniovi.wichatwebapp.entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.*;
import java.util.List;

@Document(
        collection = "answers"
)
public class Answer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private String language;
    @OneToMany(mappedBy = "correctAnswer", fetch = FetchType.EAGER)
    private List<Question> questions;

    @ManyToMany(mappedBy = "answers", fetch = FetchType.EAGER)
    private List<Question> questionsWithThisAnswer;

    public Answer(String text, String language) {
        this.text = text;
        this.language = language;
    }

    public Answer() {
    }

}
