package com.uniovi.wichatwebapp.entities;

import java.util.*;

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
    private final List<List<LetterFeedback>> feedbackHistory = new ArrayList<>();

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
        List<LetterFeedback> feedback = new ArrayList<>(Collections.nCopies(attempt.length(), LetterFeedback.WRONG));
        Map<Character, Integer> letterCounts = new HashMap<>();

        // Contar letras del target
        for (char c : targetWord.toCharArray()) {
            letterCounts.put(c, letterCounts.getOrDefault(c, 0) + 1);
        }

        // Primera pasada: marcar las correctas
        for (int i = 0; i < attempt.length(); i++) {
            char c = attempt.charAt(i);
            if (c == targetWord.charAt(i)) {
                feedback.set(i, LetterFeedback.CORRECT);
                letterCounts.put(c, letterCounts.get(c) - 1);
            }
        }

        // Segunda pasada: marcar misplaced
        for (int i = 0; i < attempt.length(); i++) {
            char c = attempt.charAt(i);
            if (feedback.get(i) == LetterFeedback.WRONG && letterCounts.getOrDefault(c, 0) > 0) {
                feedback.set(i, LetterFeedback.MISPLACED);
                letterCounts.put(c, letterCounts.get(c) - 1);
            }
        }

        return feedback;
    }

}
