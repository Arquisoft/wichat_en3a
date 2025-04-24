package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.*;
import com.uniovi.wichatwebapp.repositories.HintRepository;
import entities.Question;
import entities.QuestionCategory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class AkinatorService {
    private final HintRepository hintRepository;
    private final QuestionService questionService;
    private AkinatorGame game;

    public AkinatorService(HintRepository hintRepository, QuestionService questionService) {
        this.hintRepository = hintRepository;
        this.questionService = questionService;
    }

    public void start(QuestionCategory category, String mode) {
        if(mode.equals("ai")) {
            game = new AkinatorIAGuessesGame(category);
            game.setAiMessage(askToAiQuestion()); //ask question to the user
        } else {
            String solution="";
            do {
                Question question = questionService.getRandomQuestion(category);
                 solution = question.getCorrectAnswer().getText();
            } while (solution.matches("\\d+(\\.\\d+)?"));
            game = new AkinatorPlayerGuessesGame(category, solution);
            game.setAiMessage("Make a question");
        }
    }

    public boolean isAiGuessing(){
        return game.isAIGuessining();
    }

    public String getAiMessage(){
        return game.getAiMessage();
    }

    public String askToAiQuestion(){ //AI guesses
        String setupMessageChat = game.getSetUpMessageChat();
        return hintRepository.askWithInstructions(setupMessageChat, "", "", game.getAlreadyQuestionsAndAnswers());
    }

    public String askToAiAnswer(String question){ //Player guesses
        String setupMessageChat = game.getSetUpMessageChat();
        return hintRepository.askWithInstructions(setupMessageChat, question, "", game.getAlreadyQuestionsAndAnswers()); //yes, No...
    }


    public void askQuestion(String question) { //Player is guessing
        game.setAiMessage(askToAiAnswer(question)); //Yes, No to the question of the user
        game.addQuestionsAndAnswers(question, getAiMessage());
    }

    public void answer(String option) { //AI is guessing
        game.addQuestionsAndAnswers(getAiMessage(), option);
        game.setAiMessage(askToAiQuestion()); //ask to player another hint
    }

    public void endGame() {
        game.endGame();
    }
}
