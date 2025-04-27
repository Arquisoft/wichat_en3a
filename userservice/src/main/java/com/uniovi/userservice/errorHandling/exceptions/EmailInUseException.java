package com.uniovi.userservice.errorHandling.exceptions;

public class EmailInUseException extends RuntimeException {
    public EmailInUseException(String email) {
        super("Email "+email+" is already in use");
    }
}
