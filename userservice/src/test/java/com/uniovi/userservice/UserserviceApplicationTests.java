package com.uniovi.userservice;

import com.uniovi.userservice.controller.UserController;
import com.uniovi.userservice.entities.User;
import org.bson.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserserviceApplicationTests {

    @Autowired
    private UserController uc;

    @Test
    void testAddingNewUser() {
        User u = new User("Ana","ana@gmail.com","33011-ana",true);

        Assertions.isTrue("The user wasn't added", u.equals(uc.addUser(u)));
    }

    @Test
    void testFindUserByEmail() {
        User u = new User("Ana","ana@gmail.com","33011-ana",true);
        uc.addUser(u);

        Assertions.isTrue("The user couldn't be found", u.equals(uc.findUser("ana@gmail.com")));
    }

}
