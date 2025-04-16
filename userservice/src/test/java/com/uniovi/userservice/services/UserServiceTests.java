package com.uniovi.userservice.services;


import com.uniovi.userservice.repository.UserRepository;
import com.uniovi.userservice.service.UserService;
import entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testFindByEmail(){
        //Test data
        User user = new User("Test", "test@test.com", "testPw", true);
        //Mock repository response
        when(userRepository.findByEmail("test@test.com")).thenReturn(user);
        //Execute method
        User result = userService.findByEmail("test@test.com");

        //Check results

        assertEquals(user, result);

    }


    @Test
    //Check that when we don't find the user, it returns a not correct User
    void testFindByEmailFail(){

        //Mock repository response
        when(userRepository.findByEmail("test@test.com")).thenReturn(null);

        //Execute method
       User result = userService.findByEmail("test@test.com");

       //Check results
        assertFalse(result.isCorrect());

    }

    @Test
    void testAddUser(){
        //Test data
        User user = new User("Test", "test@test.com", "testPw", false);

        //Mock repository response
        when(userRepository.findByEmail("test@test.com")).thenReturn(null);

        //Execute method
        User result = userService.addUser(user);

        //Check results
        assertTrue(result.isCorrect());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void testAddUserAlreadyInSys(){
        //Test data
        User user = new User("Test", "test@test.com", "testPw", false);

        //Mock repository response
        when(userRepository.findByEmail("test@test.com")).thenReturn(user);

        //Execute method
        User result = userService.addUser(user);

        //Check results
        assertFalse(result.isCorrect());

    }

}
