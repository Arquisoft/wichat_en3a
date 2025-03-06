package com.uniovi.wikidataservice.service;

import com.uniovi.wikidataservice.entities.Answer;
import com.uniovi.wikidataservice.entities.Question;
import com.uniovi.wikidataservice.repositories.AnswerRepository;
import com.uniovi.wikidataservice.repositories.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public QuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        assignAnswers();

    }

    public Question findQuestionById(String id) {
        return questionRepository.findById(id).get();
    }

    public void checkAnswer(String id) {
        //TODO implement
    }
    public void assignAnswers(){
        List<Question> questions = questionRepository.findAll();
        for (Question question : questions) {
            loadAnswers(question);
        }
    }
    /**
     * Load the answers for a question (The distractors and the correct one)
     * @param question The question to load the answers for
     */
    private void loadAnswers(Question question) {
        // Create the new answers list with the distractors
        if(question.getAnswers().size() > 1) {
            return;
        }
        List<Answer> answers= answerRepository.findWrongAnswers(question.getLanguage(), question.getCorrectAnswer().getText(), 3);

         // Add the correct
        answers.add(question.getCorrectAnswer());

        // Shuffle the answers
        Collections.shuffle(answers);

        question.setAnswers(answers);
        questionRepository.save(question);
    }
}
