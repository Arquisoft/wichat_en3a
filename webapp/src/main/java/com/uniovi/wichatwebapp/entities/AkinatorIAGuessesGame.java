package com.uniovi.wichatwebapp.entities;

import java.util.HashMap;

public class AkinatorIAGuessesGame extends AkinatorGame{

    public AkinatorIAGuessesGame(QuestionCategory category) {
        super(category);
    }

    @Override
    public boolean isAIGuessining() {
        return true;
    }

    @Override
    public String getSetUpMessageChat() {
        return "You are part of a web game with same style as Akinator. Your role is to guess what the user is thinking. " +
                "You will receive a message in the following format: `;;<already given questions by you and answers of the user. They are separated by '_' character. The format is the question first, and then the answer after to that question >`. " +
                "Based on this, you must ask a question according to the following rules:\n\n" +

                "1. You cannot ask a question that was already asked (I provided it to you the questions) \n" +
                "2. The user is thinking of something in the "+ getCategory().toString() + " category.\n" +
                "3. You must only tell the question you want to ask, nothing more\n"+
                "4. The question must be a Yes or No question\n"+
                "5. In the already given questions by you and answers of the user the structure is like: question1_answer1_question2_answer2...The answer of question1 is answer1\n"+
                "6. The think that the user is thinking must satisfy all the answers of the questions\n"+
                "Remember: You must try to guess what the user is thinking.";
    }

}
