package com.uniovi.wichatwebapp.repositories;

import com.uniovi.wichatwebapp.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

@Repository
public class UserRepository {

    private WebClient.Builder webClientBuilder;

    @Value("${userservice.url}")
    private String baseUrl;

    public UserRepository(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public User getUserByEmail(String email){
        return webClientBuilder
                .baseUrl(baseUrl) // Set base URL here or in config
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
                        .baseUrl(baseUrl)
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
