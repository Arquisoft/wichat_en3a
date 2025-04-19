package com.uniovi.wichatwebapp.entities;

import java.util.ArrayList;
import java.util.List;

public class Wordle {

    public enum GameStatus {
        PLAYING, WIN, LOSE
    }

    public enum LetterFeedback {
        CORRECT, MISPLACED, WRONG
    }

    private String targetWord;
    private List<String> attempts = new ArrayList<>();
    private int maxAttempts;
    private GameStatus status = GameStatus.PLAYING;
    private List<List<LetterFeedback>> feedbackHistory = new ArrayList<>();

    public Wordle(String targetWord) {
        this.targetWord = targetWord.toUpperCase();
        maxAttempts = targetWord.length();
    }

    public List<String> getAttempts() {
        return attempts;
    }

    public void setAttempts(List<String> attempts) {
        this.attempts = attempts;
    }

    public List<List<LetterFeedback>> getFeedbackHistory() {
        return feedbackHistory;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public void setTargetWord(String targetWord) {
        this.targetWord = targetWord.toUpperCase();
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public int getRemainingAttempts() {
        return maxAttempts - attempts.size();
    }

    public void guess(String attempt) {
        if (status != GameStatus.PLAYING || attempts.size() >= maxAttempts) {
            return;
        }

        attempt = attempt.toUpperCase();
        attempts.add(attempt);
        feedbackHistory.add(getFeedback(attempt));

        if (attempt.equals(targetWord)) {
            status = GameStatus.WIN;
        } else if (attempts.size() >= maxAttempts) {
            status = GameStatus.LOSE;
        }
    }

    private List<LetterFeedback> getFeedback(String attempt) {
        List<LetterFeedback> feedback = new ArrayList<>();
        for (int i = 0; i < attempt.length(); i++) {
            char c = attempt.charAt(i);
            if (c == targetWord.charAt(i)) {
                feedback.add(LetterFeedback.CORRECT);
            } else if (targetWord.contains(String.valueOf(c))) {
                feedback.add(LetterFeedback.MISPLACED);
            } else {
                feedback.add(LetterFeedback.WRONG);
            }
        }
        return feedback;
    }

}
