package com.uniovi.wichatwebapp.validators;

import com.uniovi.wichatwebapp.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@ExtendWith(MockitoExtension.class)
public class SignUpValidatorTest {
    private SignUpValidator validator;
    private User user;
    private Errors errors;

    @BeforeEach
    void setUp() {
        validator = new SignUpValidator();
        user = new User();
        errors = new BeanPropertyBindingResult(user, "user");
    }

    @Test
    void supportsTest() {
        Assertions.assertTrue(validator.supports(User.class));
        Assertions.assertFalse(validator.supports(Object.class));
    }

    @Test
    void validateEmptyEmailTest() {
        user.setName("Test User");
        user.setPassword("password123");

        validator.validate(user, errors);

        Assertions.assertTrue(errors.hasErrors());
        Assertions.assertNotNull(errors.getFieldError("email"));
        Assertions.assertEquals("The email is required", errors.getFieldError("email").getDefaultMessage());
    }

    @Test
    void validateEmptyPasswordTest() {
        user.setName("Test User");
        user.setEmail("test@example.com");

        validator.validate(user, errors);

        Assertions.assertTrue(errors.hasErrors());
        Assertions.assertNotNull(errors.getFieldError("password"));
        Assertions.assertEquals("The password is required", errors.getFieldError("password").getDefaultMessage());
    }

    @Test
    void validateEmptyNameTest() {
        user.setEmail("test@example.com");
        user.setPassword("password123");

        validator.validate(user, errors);

        Assertions.assertTrue(errors.hasErrors());
        Assertions.assertNotNull(errors.getFieldError("name"));
        Assertions.assertEquals("The name is required", errors.getFieldError("name").getDefaultMessage());
    }

    @Test
    void validateInvalidEmailFormatTest() {
        user.setName("Test User");
        user.setEmail("invalid-email");
        user.setPassword("password123");

        validator.validate(user, errors);

        Assertions.assertTrue(errors.hasErrors());
        Assertions.assertNotNull(errors.getFieldError("email"));
        Assertions.assertEquals("The email is not valid", errors.getFieldError("email").getDefaultMessage());
    }

    @Test
    void validateValidUserTest() {
        user.setName("Test User");
        user.setEmail("valid@example.com");
        user.setPassword("password123");

        validator.validate(user, errors);

        Assertions.assertFalse(errors.hasErrors());
    }

    @Test
    void validateSeveralErrorsTest() {
        validator.validate(user, errors);

        Assertions.assertTrue(errors.hasErrors());
        Assertions.assertEquals(4, errors.getErrorCount()); // email, password, name
    }
}
