package com.uniovi.wichatwebapp.errorHandling.exceptions;

public class QuestionNotFoundException extends RuntimeException {
    public QuestionNotFoundException() {
        super("The question could not be found");
    }
}
