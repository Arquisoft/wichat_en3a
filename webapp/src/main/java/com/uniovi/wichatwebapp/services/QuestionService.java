package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.repositories.QuestionRepository;
import org.springframework.stereotype.Service;


@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    public Question getRandomQuestion() {
       return questionRepository.getRandomQuestion();
    }

    public boolean checkAnswer(String id, Question question) {
        return questionRepository.checkAnswer(question,id);
    }

    public void removeQuestion(Question question) {
        questionRepository.removeQuestion(question.getId());
    }
}
