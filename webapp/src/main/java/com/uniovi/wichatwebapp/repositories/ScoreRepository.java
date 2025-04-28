package com.uniovi.wichatwebapp.repositories;

import entities.Score;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class ScoreRepository {

    private WebClient.Builder webClientBuilder;

    public ScoreRepository(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Value("${userservice.url}")
    private String baseUrl;

    public Score addScore(Score score) {

        try{
            return webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .post()
                    .uri("/addScore")
                    .bodyValue(score)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response-> Mono.error(new RuntimeException("Error while adding score")))
                    .bodyToMono(Score.class)
                    .block();
        }catch (RuntimeException e){
            return null; //Service will handle the null
        }

    }

    public List<Score> getBestScores(String email) {
        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/getBestScores")
                        .queryParam("user", email)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Score>>() {})
                .block();
    }

    public Score getScore(String id) {
        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/getScore")
                        .queryParam("id", id)
                        .build())
                .retrieve()
                .bodyToMono(Score.class)
                .block();
    }
}
