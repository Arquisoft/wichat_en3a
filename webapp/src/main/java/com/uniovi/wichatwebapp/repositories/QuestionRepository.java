package com.uniovi.wichatwebapp.repositories;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
@Repository
public class QuestionRepository {
    private WebClient.Builder webClientBuilder;
    @Value("${wikidataservice.url}")
    private String baseUrl;

    public QuestionRepository(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Question getRandomQuestion(QuestionCategory category){
        return webClientBuilder
                .baseUrl(baseUrl) // Set base URL here or in config
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/game/newQuestion/"+category)
                        .build())
                .retrieve()
                .bodyToMono(Question.class)
                .block();
    }

    public Question getQuestion(String id){
        return webClientBuilder
                .baseUrl(baseUrl) // Set base URL here or in config
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/game/getQuestion")
                        .queryParam("id", id)
                        .build())
                .retrieve()
                .bodyToMono(Question.class)
                .block();
    }

    public boolean checkAnswer(Question question , String id){

        Answer correct =
                webClientBuilder
                        .baseUrl(baseUrl)
                        .build()
                        .get()
                        .uri(  "/game/getCorrectAnswer/"+question.getId())
                        .retrieve()
                        .bodyToMono(Answer.class)
                        .block();

        Answer chosen =
                webClientBuilder
                        .baseUrl(baseUrl)
                        .build()
                        .get()
                        .uri("/game/getAnswer/"+ id)
                        .retrieve()
                        .bodyToMono(Answer.class)
                        .block();

        if(!chosen.getText().equals(correct.getText()) ){
            return false;
        } else{
            return true;
        }
    }
    public void removeQuestion(String id){
        webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/game/removeQuestion")
                        .queryParam("id", id)
                        .build())
                .retrieve();
    }
}
