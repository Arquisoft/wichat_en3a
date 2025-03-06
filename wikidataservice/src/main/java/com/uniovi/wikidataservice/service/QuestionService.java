package com.uniovi.wikidataservice.service;

import com.uniovi.wikidataservice.entities.Answer;
import com.uniovi.wikidataservice.entities.Question;
import com.uniovi.wikidataservice.repositories.AnswerRepository;
import com.uniovi.wikidataservice.repositories.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
        List<Answer> answers= answerRepository.findWrongAnswers(question.getLanguage(), question.getCorrectAnswer().getText());

        // Shuffle the list to randomize the order
        Collections.shuffle(answers, new Random());

        // Return the first 'limit' answers
        answers =  answers.stream().limit(3).collect(Collectors.toList());
         // Add the correct
        answers.add(question.getCorrectAnswer());

        // Shuffle the answers
        Collections.shuffle(answers);

        question.setAnswers(answers);
        questionRepository.save(question);
    }


    public void save(Question question) {
        questionRepository.save(question);
    }

    public Question getRandomQuestion(String language) {
        List<Answer> answers = answerRepository.findAnswersByLanguage(language);
        List<Question> questions = questionRepository.findQuestionsByCorrectAnswerIdExists();

        // Perform the join operation in the application code
        List<Question> validQuestions = questions.stream()
                .filter(q -> answers.stream().anyMatch(a -> a.getId().equals(q.getCorrectAnswerId())))
                .collect(Collectors.toList());

        // Shuffle the list to randomize the order
        Collections.shuffle(validQuestions, new Random());

        // Return one random question
        return validQuestions.stream().findFirst().get();
    }

    public void saveAllQuestions(List<Question> questions) {
        questionRepository.saveAll(new ArrayList<>(questions));
    }

    public void saveAllAnswers(List<Answer> answers) {
        answerRepository.saveAll(new ArrayList<>(answers));
    }

    /**
     * Erases all the data in the database
     * Only used for testing
     */
    public void eraseAll(){
        questionRepository.deleteAll();
        answerRepository.deleteAll();
    }
}
