package com.uniovi.wichatwebapp.entitites;

import entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Fail.fail;

@ExtendWith(MockitoExtension.class)
public class UserTest {
    @Test
    void defaultConstructorTest() {
        User user = new User();

        Assertions.assertNull(user.getId());
        Assertions.assertNull(user.getName());
        Assertions.assertNull(user.getEmail());
        Assertions.assertNull(user.getPassword());
        Assertions.assertFalse(user.isCorrect());
    }

    @Test
    void fullConstructorTest() {
        User user = new User("John Doe", "john@example.com", "secure123", true);

        Assertions.assertEquals("John Doe", user.getName());
        Assertions.assertEquals("john@example.com", user.getEmail());
        Assertions.assertEquals("secure123", user.getPassword());
        Assertions.assertTrue(user.isCorrect());
        Assertions.assertNull(user.getId()); // ID not set in constructor
    }

    @Test
    void settersAndGettersTest() {
        User user = new User();

        // Set all fields
        user.setId("user123");
        user.setName("Alice Smith");
        user.setEmail("alice@example.com");
        user.setPassword("newPassword");
        user.setCorrect(false);

        // Verify all fields
        Assertions.assertEquals("user123", user.getId());
        Assertions.assertEquals("Alice Smith", user.getName());
        Assertions.assertEquals("alice@example.com", user.getEmail());
        Assertions.assertEquals("newPassword", user.getPassword());
        Assertions.assertFalse(user.isCorrect());
    }
}
