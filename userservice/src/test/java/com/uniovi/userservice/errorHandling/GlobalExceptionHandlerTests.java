package com.uniovi.userservice.errorHandling;

import com.uniovi.userservice.errorHandling.exceptions.EmailInUseException;
import com.uniovi.userservice.errorHandling.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTests {

    private GlobalExceptionHandler geh = new GlobalExceptionHandler();

    @Test
    public void testUserNotFoundException() {

        ResponseEntity<Map<String, Object>> response = geh.handleException(new UserNotFoundException("Test"));


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User with email Test not found", response.getBody().get("message"));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().get("status"));
    }

    @Test
    public void testEmailInUseException() {

        ResponseEntity<Map<String, Object>> response = geh.handleException(new EmailInUseException("Test@test.com"));


        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email Test@test.com is already in use", response.getBody().get("message"));
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().get("status"));
    }

}
