package com.uniovi.wichatwebapp.errorHandling;

import com.uniovi.wichatwebapp.errorHandling.exceptions.AnswerNotFound;
import com.uniovi.wichatwebapp.errorHandling.exceptions.QuestionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String,Object>> handleException(AnswerNotFound ex) {
        Map<String,Object> map = new HashMap<>();
        map.put("message", ex.getMessage());
        map.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,Object>> handleException(MethodArgumentTypeMismatchException ex) {
        Map<String,Object> map = new HashMap<>();
        map.put("message", "This category does not exist");
        map.put("status", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,Object>> handleException(QuestionNotFoundException ex) {
        Map<String,Object> map = new HashMap<>();
        map.put("message", ex.getMessage());
        map.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }
}
