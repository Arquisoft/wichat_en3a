package com.uniovi.wichatwebapp.service;


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

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class InsertSampleDataMultithreadedService {
    private final QuestionService questionService;

    public InsertSampleDataMultithreadedService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostConstruct
    public void init() {
        // Erases the database. Mainly for testing
        questionService.eraseAll();

        CompletableFuture<Void> flagQuestions = insertFlagQuestions();
        CompletableFuture<Void> movieQuestions = insertMovieQuestions();
        CompletableFuture<Void> brandQuestions = insertBrandQuestions();
        CompletableFuture<Void> monumentCountryQuestions = insertMonumentCountryQuestions();
        CompletableFuture<Void> monumentNameQuestions = insertMonumentNameQuestions();
        CompletableFuture<Void> basketballTeamQuestions = insertBasketballTeamQuestions();
        CompletableFuture<Void> f1TeamQuestions = insertF1TeamQuestions();
        CompletableFuture<Void> footballTeamQuestions = insertFootballTeamQuestions();
        CompletableFuture<Void> teamLogoQuestions = insertTeamLogoQuestions();
        CompletableFuture<Void> whatAnimalQuestions = insertWhatAnimalQuestions();
        CompletableFuture<Void> animalScientificNameQuestions = insertAnimalScientificNameQuestions();
        CompletableFuture<Void> animalLifespanQuestions = insertAnimalLifespanQuestions();

        CompletableFuture.allOf(
                flagQuestions, movieQuestions, brandQuestions, monumentCountryQuestions, monumentNameQuestions,
                basketballTeamQuestions, f1TeamQuestions, footballTeamQuestions, teamLogoQuestions,
                whatAnimalQuestions, animalScientificNameQuestions, animalLifespanQuestions
        ).join();
    }

    @Async
    public CompletableFuture<Void> insertFlagQuestions() {
        FlagQuestion fq = new FlagQuestion("en");
        questionService.saveAllQuestions(fq.getQs());
        questionService.saveAllAnswers(fq.getAs());
        return CompletableFuture.completedFuture(null);
    }


    @Async("taskExecutor")
    public CompletableFuture<Void> insertMovieQuestions() {
        MovieQuestion mq = new MovieQuestion("en");
        questionService.saveAllQuestions(mq.getQs());
        questionService.saveAllAnswers(mq.getAs());
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> insertBrandQuestions() {
        BrandQuestion bq = new BrandQuestion("en");
        questionService.saveAllQuestions(bq.getQs());
        questionService.saveAllAnswers(bq.getAs());
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> insertMonumentCountryQuestions() {
        MonumentCountryQuestion mcq = new MonumentCountryQuestion("en");
        questionService.saveAllQuestions(mcq.getQs());
        questionService.saveAllAnswers(mcq.getAs());
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> insertMonumentNameQuestions() {
        MonumentNameQuestion mnq = new MonumentNameQuestion("en");
        questionService.saveAllQuestions(mnq.getQs());
        questionService.saveAllAnswers(mnq.getAs());
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> insertBasketballTeamQuestions() {
        BasketballTeam bbtmq = new BasketballTeam("en");
        questionService.saveAllQuestions(bbtmq.getQs());
        questionService.saveAllAnswers(bbtmq.getAs());
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> insertF1TeamQuestions() {
        F1Team f1tmq = new F1Team("en");
        questionService.saveAllQuestions(f1tmq.getQs());
        questionService.saveAllAnswers(f1tmq.getAs());
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> insertFootballTeamQuestions() {
        FootballTeam ftmq = new FootballTeam("en");
        questionService.saveAllQuestions(ftmq.getQs());
        questionService.saveAllAnswers(ftmq.getAs());
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> insertTeamLogoQuestions() {
        TeamLogo tlq = new TeamLogo("en");
        questionService.saveAllQuestions(tlq.getQs());
        questionService.saveAllAnswers(tlq.getAs());
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> insertWhatAnimalQuestions() {
        WhatAnimal waq = new WhatAnimal("en");
        questionService.saveAllQuestions(waq.getQs());
        questionService.saveAllAnswers(waq.getAs());
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> insertAnimalScientificNameQuestions() {
        AnimalScientificName asnq = new AnimalScientificName("en");
        questionService.saveAllQuestions(asnq.getQs());
        questionService.saveAllAnswers(asnq.getAs());
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> insertAnimalLifespanQuestions() {
        AnimalLifespan anls = new AnimalLifespan("en");
        questionService.saveAllQuestions(anls.getQs());
        questionService.saveAllAnswers(anls.getAs());
        return CompletableFuture.completedFuture(null);
    }
}


