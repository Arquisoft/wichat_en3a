package com.uniovi.wichatwebapp.validators;

import entities.User;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SignUpValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.empty", "The email is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty", "The password is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "The name is required");

        if(!EmailValidator.getInstance().isValid(user.getEmail())) {
            errors.rejectValue("email", "email.invalid", "The email is not valid");
        }
    }
}
