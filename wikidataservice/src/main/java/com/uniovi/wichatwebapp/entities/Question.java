package com.uniovi.wichatwebapp.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Document(
        collection = "questions"
)
public class Question {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name="questions_answers",
                joinColumns=
                @JoinColumn(name="question_id", referencedColumnName="id"),
                inverseJoinColumns=
                @JoinColumn(name="answer_id", referencedColumnName="id")
        )
        private List<Answer> answers;
        @ManyToOne
        @JoinColumn(name = "correct_answer_id")
        private Answer correctAnswer;
        private String content;

    public Question() {
    }

    public Question(Answer correctAnswer, String content) {
        this.correctAnswer = correctAnswer;
        this.content = content;
        this.answers = new ArrayList<>();
        this.answers.add(correctAnswer);
    }

        public List<Answer> getAnswers() {
        return answers;
    }


}
