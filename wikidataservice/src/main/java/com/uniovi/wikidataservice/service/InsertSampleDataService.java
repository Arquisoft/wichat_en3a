package com.uniovi.wikidataservice.service;


import com.uniovi.wikidataservice.entities.Question;
import com.uniovi.wikidataservice.wikidata.FlagQuestion;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;


@Service
public class InsertSampleDataService {
    private final QuestionService questionService;
    public InsertSampleDataService( QuestionService questionService) {
        this.questionService = questionService;
    }
    @PostConstruct
    public void init() {
        for(int i = 0; i<10; i++){
           // new FlagQuestion("en"); TODO
        }




    }
}

