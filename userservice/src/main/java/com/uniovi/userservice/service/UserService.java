package com.uniovi.userservice.service;

import com.uniovi.userservice.entities.User;
import com.uniovi.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service that manages the users' logic
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email){
        User user = userRepository.findByEmail(email);

        if(user == null){
            user = new User();
            user.setCorrect(false);

        }

        return user;
    }

    public User addUser(User user){

        User checkUser = userRepository.findByEmail(user.getEmail());
        if(checkUser != null){
            user.setCorrect(false);
        }else{
            user.setCorrect(true);
            userRepository.save(user);
        }
        return user;

    }

}
