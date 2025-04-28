package com.uniovi.wichatwebapp.wikidata.biology;


import com.uniovi.wichatwebapp.wikidata.SimpleQuestion;
import entities.AnswerCategory;
import entities.QuestionCategory;

public class WhatAnimal extends SimpleQuestion {


    public WhatAnimal(String langCode) {
        super(langCode);
    }

    @Override
    protected void initStringsIni() {
        spanishStringsIni = new String[]{"¿Qué animal es este?", "¿Cuál es este animal?"};
        englishStringsIni= new String[]{"What is this animal?", "What's the name of this animal?"};
    }

    //For testing
    public WhatAnimal(){
        super();
    }
    @Override
    public void setQuery() {
        this.sparqlQuery =
                "SELECT DISTINCT ?commonName ?image WHERE {\n" +
                        "  ?animal wdt:P1034 ?foodSource . \n" +
                        "  ?animal wdt:P31 wd:Q16521 .  \n" +
                        "  ?animal wikibase:sitelinks ?sitelinks .\n" +
                        "  ?animal wdt:P18 ?image\n" +
                        "  FILTER(?sitelinks < 100)\n" +
                        "  FILTER(?sitelinks > 50) \n" +
                        "\n" +
                        "  ?animal rdfs:label ?name .\n" +
                        "  FILTER(LANG(?name) = \"en\")  \n" +
                        "\n" +
                        "  ?animal p:P1843 ?commonNameStatement .  \n" +
                        "  ?commonNameStatement ps:P1843 ?commonName . \n" +
                        "  FILTER(LANG(?commonName) = \"en\")\n" +
                        "}\n" +
                        "ORDER BY DESC(?sitelinks)\n" +
                        "LIMIT 100";
    }

    @Override
    protected QuestionCategory getQuestionCategory() {
        return QuestionCategory.BIOLOGY;
    }

    @Override
    protected AnswerCategory getAnswerCategory() {
        return AnswerCategory.ANIMAL_NAME;
    }

    @Override
    protected String getAnswerLabel() {
        return "commonName";
    }

}
