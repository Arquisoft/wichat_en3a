package com.uniovi.wichatwebapp.service;


import com.uniovi.wichatwebapp.wikidata.FlagQuestion;
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

        //Erases the database. Mainly for testing
        questionService.eraseAll();

        for(int i = 0; i<1; i++){
            FlagQuestion fq = new FlagQuestion("en");

            questionService.saveAllQuestions(fq.getQs());
            questionService.saveAllAnswers(fq.getAs());

        }




    }
}

