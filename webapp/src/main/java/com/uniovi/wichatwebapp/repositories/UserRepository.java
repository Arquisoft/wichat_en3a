package com.uniovi.wichatwebapp.repositories;

import com.uniovi.wichatwebapp.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

@Repository
public class UserRepository {

    private WebClient.Builder webClientBuilder;

    public UserRepository(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

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
