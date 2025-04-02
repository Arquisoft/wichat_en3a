package com.uniovi.wichatwebapp.service;


import com.uniovi.wichatwebapp.wikidata.biology.AnimalLifespan;
import com.uniovi.wichatwebapp.wikidata.biology.AnimalScientificName;
import com.uniovi.wichatwebapp.wikidata.biology.WhatAnimal;
import com.uniovi.wichatwebapp.wikidata.geography.MonumentCountryQuestion;
import com.uniovi.wichatwebapp.wikidata.geography.MonumentNameQuestion;
import com.uniovi.wichatwebapp.wikidata.pop.BrandQuestion;
import com.uniovi.wichatwebapp.wikidata.geography.FlagQuestion;
import com.uniovi.wichatwebapp.wikidata.pop.MovieQuestion;
import com.uniovi.wichatwebapp.wikidata.sports.BasketballTeam;
import com.uniovi.wichatwebapp.wikidata.sports.F1Team;
import com.uniovi.wichatwebapp.wikidata.sports.FootballTeam;
import com.uniovi.wichatwebapp.wikidata.sports.TeamLogo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;



public class InsertSampleDataService {
    private final QuestionService questionService;
    public InsertSampleDataService( QuestionService questionService) {
        this.questionService = questionService;
    }
    @PostConstruct
    public void init() {

        //Erases the database. Mainly for testing
        questionService.eraseAll();

        for(int i = 0; i<1; i++){
            FlagQuestion fq = new FlagQuestion("en");

            questionService.saveAllQuestions(fq.getQs());
            questionService.saveAllAnswers(fq.getAs());

            MovieQuestion mq = new MovieQuestion("en");
            questionService.saveAllQuestions(mq.getQs());
            questionService.saveAllAnswers(mq.getAs());

            BrandQuestion bq = new BrandQuestion("en");
            questionService.saveAllQuestions(bq.getQs());
            questionService.saveAllAnswers(bq.getAs());

            MonumentCountryQuestion mcq = new MonumentCountryQuestion("en");
            questionService.saveAllQuestions(mcq.getQs());
            questionService.saveAllAnswers(mcq.getAs());

            MonumentNameQuestion mnq = new MonumentNameQuestion("en");
            questionService.saveAllQuestions(mnq.getQs());
            questionService.saveAllAnswers(mnq.getAs());

            BasketballTeam bbtmq = new BasketballTeam("en");
            questionService.saveAllQuestions(bbtmq.getQs());
            questionService.saveAllAnswers(bbtmq.getAs());

            F1Team f1tmq = new F1Team("en");
            questionService.saveAllQuestions(f1tmq.getQs());
            questionService.saveAllAnswers(f1tmq.getAs());

            FootballTeam ftmq = new FootballTeam("en");
            questionService.saveAllQuestions(ftmq.getQs());
            questionService.saveAllAnswers(ftmq.getAs());

            TeamLogo tlq = new TeamLogo("en");
            questionService.saveAllQuestions(tlq.getQs());
            questionService.saveAllAnswers(tlq.getAs());

            WhatAnimal waq = new WhatAnimal("en");
            questionService.saveAllQuestions(waq.getQs());
            questionService.saveAllAnswers(waq.getAs());

            AnimalScientificName asnq = new AnimalScientificName("en");
            questionService.saveAllQuestions(asnq.getQs());
            questionService.saveAllAnswers(asnq.getAs());

            AnimalLifespan anls = new AnimalLifespan("en");
            questionService.saveAllQuestions(anls.getQs());
            questionService.saveAllAnswers(anls.getAs());
        }




    }
}

