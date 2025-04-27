package com.uniovi.wichatwebapp.errorHandling.exceptions;

public class AnswerNotFound extends RuntimeException {
    public AnswerNotFound() {
        super("The answer could not be found");
    }
}
