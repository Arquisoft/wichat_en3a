package com.uniovi.wichatwebapp.entities;

import entities.QuestionCategory;

import java.util.HashMap;
import java.util.Map;

public abstract class AkinatorGame {
    private final QuestionCategory category;
    private HashMap<String,String> questionsAndAnswers=new HashMap<>();

    private String AiMessage;

    public AkinatorGame(QuestionCategory category) {
        this.category=category;
    }

    public QuestionCategory getCategory() {
        return category;
    }

    public void addQuestionsAndAnswers(String question, String answer) {
        questionsAndAnswers.put(question, answer);
    }

    public abstract boolean isAIGuessining();

    public String getAiMessage() {
        return AiMessage;
    }
    public void setAiMessage(String aiMessage) {
        AiMessage = aiMessage;
    }

    public abstract String getSetUpMessageChat();

    public String getAlreadyQuestionsAndAnswers() {
        StringBuilder stringBuilder=new StringBuilder();

        for (Map.Entry<String, String> entry : questionsAndAnswers.entrySet()) {
            stringBuilder.append(entry.getKey())
                    .append("_")
                    .append(entry.getValue())
                    .append("_");  // New line after each Q&A pair
        }
        return stringBuilder.toString();
    }

    public void endGame() {

    }
}
