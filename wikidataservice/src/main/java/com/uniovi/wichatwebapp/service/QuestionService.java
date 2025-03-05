package com.uniovi.wichatwebapp.service;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.repositories.AnswerRepository;
import com.uniovi.wichatwebapp.repositories.QuestionRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
