package com.uniovi.wichatwebapp.service;


import com.uniovi.wichatwebapp.repositories.AnswerRepository;
import com.uniovi.wichatwebapp.repositories.QuestionRepository;
import entities.Answer;
import entities.AnswerCategory;
import entities.Question;
import entities.QuestionCategory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private boolean areAssigned = false;
    private final SecureRandom random = new SecureRandom();

    public QuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;

    }

    public Question findQuestionById(String id) {
        Optional<Question> question = questionRepository.findById(id);
        return question.orElse(null);
    }


    public boolean checkAnswer(String id, Question question) {

        return(question.getCorrectAnswerId().equals(id));
    }

    public void assignAnswers() {
        areAssigned = true;
        List<Question> questions = questionRepository.findAll();
        for (Question question : questions) {
            loadAnswers(question);
        }
    }

    /**
     * Load the answers for a question (Correct and distractors).
     *
     * @param question The question to load the answers for
     */
    private void loadAnswers(Question question) {
        // Skip questions that already have answers assigned
        if (question.getAnswers().size() > 1) {
            return;
        }

        // Retrieve the correct answer and its category
        AnswerCategory answerCategory = question.getCorrectAnswer().getCategory();
        List<Answer> wrongAnswers = answerRepository.findWrongAnswers(
                question.getLanguage(),
                question.getCorrectAnswer().getText(),
                answerCategory
        );

        // Shuffle wrong answers and limit to 3 with different getText
        Collections.shuffle(wrongAnswers, new Random());
        List<Answer> selectedWrongAnswers = wrongAnswers.stream()
                .distinct() // Ensure uniqueness
                .filter(a -> a.getText() != null) // Additional safeguard if needed
                .limit(3)
                .collect(Collectors.toList());

        // Add the correct answer to the answer list
        selectedWrongAnswers.add(question.getCorrectAnswer());

        // Shuffle the final list of answers
        Collections.shuffle(selectedWrongAnswers, new Random());

        // Set answers in the question and save
        question.setAnswers(selectedWrongAnswers);
        questionRepository.save(question);
    }

    public void save(Question question) {
        questionRepository.save(question);
    }

    /**
     * Get one random question that matches the language and category.
     *
     * @param language The language for the question
     * @param category The category of the question
     * @return A random question
     */
    public Question getRandomQuestion(String language, QuestionCategory category) {
        if(!areAssigned) {
            assignAnswers();
        }
        // Match categories to find applicable answers
        List<AnswerCategory> answerCategories = matchCategories(category);
        List<Answer> answers = answerRepository.findAnswersByLanguageAndQuestionCategory(language, answerCategories);
        List<Question> questions = questionRepository.findQuestionsByCorrectAnswerIdExists();

        // Filter questions to match those with valid answers and the specified category
        List<Question> validQuestions = questions.stream()
                .filter(q -> answers.stream().anyMatch(a -> a.getId().equals(q.getCorrectAnswerId())))
                .filter(q -> q.getCategory().equals(category)) // Match the specific category
                .collect(Collectors.toList());

        // Shuffle the list to randomize the order and pick one
        Collections.shuffle(validQuestions, new Random());

        if (!validQuestions.isEmpty()) {
            Question randomQuestion = validQuestions.getFirst(); // Get the first random question

            // Ensure the question has a set of valid answers (correct and distractors)
            List<Answer> possibleAnswers = new ArrayList<>();
            int count = 1;
            Collections.shuffle(answers, new Random());
            for (Answer answer : answers) {
                if (count < 4) {
                    if (!answer.getText().equals(randomQuestion.getCorrectAnswer().getText())
                            && answer.getCategory().equals(randomQuestion.getCorrectAnswer().getCategory())) {
                        boolean isAlreadyAnswer = false;
                        for(Answer wrongAnswer : possibleAnswers) {
                            if (wrongAnswer.getText().equals(answer.getText())) {
                                isAlreadyAnswer = true;
                                break;
                            }
                        }
                        if(!isAlreadyAnswer) {
                            possibleAnswers.add(answer);
                            count++;
                        }
                    }
                } else {
                    break;
                }
            }

            // Add the correct answer and shuffle all answers
            possibleAnswers.add(randomQuestion.getCorrectAnswer());
            Collections.shuffle(possibleAnswers, new Random());

            randomQuestion.setAnswers(possibleAnswers);
            return randomQuestion;
        }

        return null; // Return null if no valid question is found
    }

    public List<AnswerCategory> matchCategories(QuestionCategory questionCategory) {
        List<AnswerCategory> answerCategories = new ArrayList<>();
        switch (questionCategory) {
            case SPORT:{
                answerCategories.add(AnswerCategory.PERSON_BASKETBALL_TEAM);
                answerCategories.add(AnswerCategory.PERSON_F1_TEAM);
                answerCategories.add(AnswerCategory.PERSON_FOOTBALL_TEAM);
                answerCategories.add(AnswerCategory.SPORT_TEAM_LOGO);
                return answerCategories;
            }
            case GEOGRAPHY:{
                answerCategories.add(AnswerCategory.FLAG);
                answerCategories.add(AnswerCategory.MONUMENT_NAME);
                answerCategories.add(AnswerCategory.MONUMENT_COUNTRY);
                return answerCategories;
            }
            case POP_CULTURE:{
                answerCategories.add(AnswerCategory.BRAND);
                answerCategories.add(AnswerCategory.MOVIE_YEAR);
                return answerCategories;
            }
            case BIOLOGY:{
                answerCategories.add(AnswerCategory.ANIMAL_NAME);
                answerCategories.add(AnswerCategory.ANIMAL_SCIENTIFIC_NAME);
                answerCategories.add(AnswerCategory.ANIMAL_LIFESPAN);
                return answerCategories;
            }
        }
        return answerCategories;
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

    public void removeQuestion(Question question){
        questionRepository.delete(question);
    }


    public Answer findAnswerById(String answerId) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        return optionalAnswer.orElse(null);
    }
/*
    @Transactional
    public void saveAllQuestionsBatch(List<Question> questions) {
        questionRepository.saveAll(questions);
    }
    @Transactional
    public void saveAllAnswersBatch(List<Answer> answers) {
        answerRepository.saveAll(answers);
    }
*/
    public Question getRandomQuestionNoCategory(String language) {

        QuestionCategory[] categories = QuestionCategory.values();
        QuestionCategory randomCategory = categories[random.nextInt(categories.length)];
        return getRandomQuestion(language,randomCategory);
    }
}
