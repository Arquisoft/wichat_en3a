package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.repositories.QuestionRepository;
import entities.Question;
import entities.QuestionCategory;
import org.springframework.stereotype.Service;


@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    public Question getRandomQuestion(QuestionCategory category) {
        return questionRepository.getRandomQuestion(category);
    }

    /*
    public boolean checkAnswer(String id, Question question) {
        return questionRepository.checkAnswer(question,id);
    }*/
    public Question getRandomQuestionNoCategory(){
        return questionRepository.getRandomQuestionNoCategory();
    }
    public void removeQuestion(Question question) {
        questionRepository.removeQuestion(question.getId());
    }
}
