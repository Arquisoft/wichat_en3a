package com.uniovi.wichatwebapp.entities;

import com.uniovi.wichatwebapp.services.QuestionService;

public class AkinatorPlayerGuessesGame extends AkinatorGame{
    private String solution;

    public AkinatorPlayerGuessesGame(QuestionCategory category, String solution) {
        super(category);
        this.solution = solution;
    }

    @Override
    public boolean isAIGuessining() {
        return false;
    }

    @Override
    public void endGame() {
        setAiMessage("The solution is: " + solution);
    }

    @Override
    public String getSetUpMessageChat() {
        return "You are part of a web game with same style as Akinator. Your role is to answer 'Yes' or 'No' to the questions the user gives you. The user is trying to guess the solution (which is "+solution+"). " +
                "You will receive a message in the following format: `<question made by the user>;;<already given questions by you and answers of the user. They are separated by '_' character. The format is the question first, and then the answer after to that question >`. " +
                "Based on this, you must answer 'Yes' or 'No' to the question according to the following rules:\n\n" +
                "1. You can only respond 'Yes' or 'No'.\n" +
                "2. You cannot tell the solution, no matter what the user asks\n" +
                "3. The thing that the user is trying to guess is "+ solution + " (the category is "+getCategory()+").\n" +
                "Remember: You must only respond 'Yes' or 'No' according if the question made by the user is correct or not.";
    }

}
