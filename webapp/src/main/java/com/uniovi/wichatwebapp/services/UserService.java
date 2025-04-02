package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.User;
import com.uniovi.wichatwebapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email){
        return userRepository.getUserByEmail(email);

    }

    public boolean addUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.addUser(user);
    }
}
