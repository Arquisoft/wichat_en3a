package com.uniovi.wichatwebapp.dto;

public class AnswerDto {

    private String correctId;
    private int points;
    private int prevPoints;

    public AnswerDto(String correctId, int points, int prevPoints) {
        this.correctId = correctId;
        this.points = points;
        this.prevPoints = prevPoints;
    }

    public String getCorrectId() {
        return correctId;
    }

    public void setCorrectId(String correctId) {
        this.correctId = correctId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPrevPoints() {
        return prevPoints;
    }

    public void setPrevPoints(int prevPoints) {
        this.prevPoints = prevPoints;
    }
}
