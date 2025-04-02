package com.uniovi.wichatwebapp.repositories;

import com.uniovi.wichatwebapp.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatusCode;

@Repository
public class UserRepository {

    private WebClient.Builder webClientBuilder;

    @Value("${userservice.url}")
    private String baseUrl;

    public UserRepository(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public User getUserByEmail(String email){
        try{
            return webClientBuilder
                    .baseUrl(baseUrl) // Set base URL here or in config
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/getUser")
                            .queryParam("email", email)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response-> Mono.error(new RuntimeException("Error while getting user")))
                    .bodyToMono(User.class)
                    .block();
        }catch (RuntimeException e){
            return  null;
        }
    }

    public boolean addUser(User user){
        try{
            User result =
                    webClientBuilder
                            .baseUrl(baseUrl)
                            .build()
                            .post()
                            .uri("/addUser")
                            .bodyValue(user)
                            .retrieve()
                            .onStatus(HttpStatusCode::is4xxClientError, response-> Mono.error(new RuntimeException("Error while adding user")))
                            .bodyToMono(User.class)
                            .block();
            if(result == null){
                return false;
            } else{
                return result.isCorrect();
            }
        }catch (RuntimeException e){
            return false;
        }
    }
}
