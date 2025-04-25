package com.uniovi.wichatwebapp.wikidata.biology;


import com.uniovi.wichatwebapp.wikidata.ComposeQuestion;
import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
import entities.AnswerCategory;
import entities.QuestionCategory;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnimalLifespan extends ComposeQuestion {


    public AnimalLifespan(String langCode) {
        super(langCode);
    }

    @Override
    protected void initStringsIni() {
        spanishStringsIni = new String[]{"¿Cuál es la esperanza de vida de %s?", "¿Cuánto suele vivir %s?"};
        englishStringsIni = new String[]{"What is the lifespan of %s?", "How long does %s usually live?"};
    }

    // For testing
    public AnimalLifespan() {
        super();
    }

    @Override
    public void setQuery() {
        this.sparqlQuery =
                "SELECT DISTINCT ?commonName ?image ?lifespan WHERE {\n" +
                        "  ?animal wdt:P1034 ?foodSource .\n" +
                        "  ?animal wdt:P31 wd:Q16521 .\n" +
                        "  ?animal wdt:P2250 ?lifespan .\n" +
                        "  ?animal wikibase:sitelinks ?sitelinks .\n" +
                        "  ?animal wdt:P18 ?image .\n" +
                        "  ?animal p:P1843 ?commonNameStatement .\n" +
                        "  ?commonNameStatement ps:P1843 ?commonName .\n" +
                        "  FILTER(LANG(?commonName) = 'en')\n" +
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
        return AnswerCategory.ANIMAL_LIFESPAN;
    }

    @Override
    protected String getAnswerLabel() {
        return "lifespan";
    }

    @Override
    protected String getQuestionLabel() {
        return "commonName";
    }


}