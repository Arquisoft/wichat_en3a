package com.uniovi.userservice.controllers;


import com.uniovi.userservice.controller.UserController;
import com.uniovi.userservice.entities.User;
import com.uniovi.userservice.service.UserService;
import org.bson.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @InjectMocks
    private UserController uc;

    @Mock
    private UserService userService;

    @Test
    void testAddingNewUser() {
        User u = new User("Ana","ana@gmail.com","33011-ana",true);

        when(userService.addUser(u)).thenReturn(u);

        User result = uc.addUser(u);

        Assertions.isTrue("The user wasn't added", u.equals(result));
    }

    @Test
    void testFindUserByEmail() {
        User u = new User("Ana","ana@gmail.com","33011-ana",true);

        when(userService.findByEmail(u.getEmail())).thenReturn(u);

        User result = uc.findUser("ana@gmail.com");

        Assertions.isTrue("The user couldn't be found", u.equals(result));
    }

}
