package com.uniovi.userservice.errorHandling;

import com.uniovi.userservice.errorHandling.exceptions.EmailInUseException;
import com.uniovi.userservice.errorHandling.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String,Object>> handleException(UserNotFoundException ex){
        Map<String,Object> map = new HashMap<>();

        map.put("message", ex.getMessage());
        map.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,Object>> handleException(EmailInUseException ex){
        Map<String,Object> map = new HashMap<>();

        map.put("message", ex.getMessage());
        map.put("status", HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }
}
