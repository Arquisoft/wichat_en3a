package com.uniovi.wichatwebapp.errorHandling;

import com.uniovi.wichatwebapp.errorHandling.exceptions.AnswerNotFound;
import com.uniovi.wichatwebapp.errorHandling.exceptions.QuestionNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTests {

    private GlobalExceptionHandler ghe = new GlobalExceptionHandler();

    @Test
    public void testAnswerNotFoundException(){

        ResponseEntity<Map<String, Object>> response = ghe.handleException(new AnswerNotFound());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("The answer could not be found", response.getBody().get("message"));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().get("status"));

    }

    @Test
    public void testQuestionNotFoundException(){
        ResponseEntity<Map<String, Object>> response = ghe.handleException(new QuestionNotFoundException());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("The question could not be found", response.getBody().get("message"));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().get("status"));
    }

    @Test
    public void testMethodArgumentTypeMismatchException(){
        ResponseEntity<Map<String, Object>> response = ghe.handleException(new MethodArgumentTypeMismatchException("test", String.class, "test", null, new RuntimeException("test")));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("This category does not exist", response.getBody().get("message"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().get("status"));
    }

}
