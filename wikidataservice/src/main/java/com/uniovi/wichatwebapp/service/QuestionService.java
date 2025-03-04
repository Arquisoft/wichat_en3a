package main.java.com.uniovi.wichatwebapp.service;

import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.repositories.QuestionRepository;

public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question findQuestionById(String id) {
        return questionRepository.findById(id);
    }
}
