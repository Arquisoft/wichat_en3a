package com.uniovi.wichatwebapp.service;


import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.biology.AnimalLifespan;
import com.uniovi.wichatwebapp.wikidata.biology.AnimalScientificName;
import com.uniovi.wichatwebapp.wikidata.biology.WhatAnimal;
import com.uniovi.wichatwebapp.wikidata.geography.FlagQuestion;
import com.uniovi.wichatwebapp.wikidata.geography.MonumentCountryQuestion;
import com.uniovi.wichatwebapp.wikidata.geography.MonumentNameQuestion;
import com.uniovi.wichatwebapp.wikidata.pop.BrandQuestion;
import com.uniovi.wichatwebapp.wikidata.pop.MovieQuestion;
import com.uniovi.wichatwebapp.wikidata.sports.BasketballTeam;
import com.uniovi.wichatwebapp.wikidata.sports.F1Team;
import com.uniovi.wichatwebapp.wikidata.sports.FootballTeam;
import com.uniovi.wichatwebapp.wikidata.sports.TeamLogo;

import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

//@Service
@EnableAsync
public class InsertSampleDataMultithreadedService {

    private final QuestionService questionService;
    public InsertSampleDataMultithreadedService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostConstruct
    public void init() {
        questionService.eraseAll();

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        futures.add(saveQuestionsAsync(new BasketballTeam("en")));
        futures.add(saveQuestionsAsync(new F1Team("en")));
        futures.add(saveQuestionsAsync(new FootballTeam("en")));
        futures.add(saveQuestionsAsync(new TeamLogo("en")));
        futures.add(saveQuestionsAsync(new FlagQuestion("en")));
        futures.add(saveQuestionsAsync(new MovieQuestion("en")));
        futures.add(saveQuestionsAsync(new BrandQuestion("en")));
        futures.add(saveQuestionsAsync(new MonumentCountryQuestion("en")));
        futures.add(saveQuestionsAsync(new MonumentNameQuestion("en")));
        futures.add(saveQuestionsAsync(new WhatAnimal("en")));
        futures.add(saveQuestionsAsync(new AnimalScientificName("en")));
        futures.add(saveQuestionsAsync(new AnimalLifespan("en")));

        // Wait for all tasks to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    @Async
    public CompletableFuture<Void> saveQuestionsAsync(QuestionWikidata questionType) {
        List<Question> questions = questionType.getQs();
        List<Answer> answers = questionType.getAs();

        questionService.saveAllQuestionsBatch(questions);
        questionService.saveAllAnswersBatch(answers);

        return CompletableFuture.completedFuture(null);
    }



}

