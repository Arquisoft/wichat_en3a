package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public User getUserByEmail(String email){
        return webClientBuilder
                .baseUrl("http://localhost:8100") // Set base URL here or in config
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/getUser")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(User.class)
                .block();

    }

    public boolean addUser(User user){
        User result =
                webClientBuilder
                        .baseUrl("http://localhost:8100")
                        .build()
                        .post()
                        .uri("/addUser")
                        .bodyValue(user)
                        .retrieve()
                        .bodyToMono(User.class)
                        .block();
        if(result == null){
            return false;
        } else{
            return result.isCorrect();
        }
    }
}
