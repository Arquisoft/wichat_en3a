package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.repositories.UserRepository;
import entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void getUserByEmailTest() {
        User testUser = new User();
        when(userRepository.getUserByEmail(any())).thenReturn(testUser);

        User user = userService.getUserByEmail("test@gmail.com");
        Assertions.assertEquals(testUser, user);
    }

    @Test
    public void addUserTest() {
        User testUser = new User();
        String originalPassword = "aaa";
        testUser.setPassword(originalPassword);

        when(userRepository.addUser(any())).thenReturn(true);
        when(passwordEncoder.encode(any())).thenReturn("encrypted");

        boolean add = userService.addUser(testUser);

        Assertions.assertTrue(add);
        Assertions.assertNotEquals(originalPassword, testUser.getPassword());
        Assertions.assertEquals(passwordEncoder.encode(originalPassword), testUser.getPassword());
    }
}
