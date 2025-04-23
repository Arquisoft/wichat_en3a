package com.uniovi.wichatwebapp.wikidata.sports;


import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.SimpleQuestion;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
import entities.AnswerCategory;
import entities.QuestionCategory;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeamLogo extends SimpleQuestion {

    public TeamLogo() {
        super();
    }


    public TeamLogo(String langCode) {
        super(langCode);
    }

    @Override
    protected void initStringsIni() {
        spanishStringsIni = new String[]{"¿De qué equipo es este escudo?", "¿Cuál es el equipo con este escudo?", "¿A qué equipo pertenece este escudo?"};
        englishStringsIni= new String[]{"What team is this logo from?", "Which team has this logo?", "To which team belongs this logo?"};
    }

    @Override
    public void setQuery() {
        this.sparqlQuery =
                "SELECT DISTINCT ?name (MAX(?image_) AS ?image) WHERE {\n" +
                        "  VALUES ?liga { wd:Q324867 wd:Q35615 wd:Q9448 wd:Q15804 wd:Q82595 }\n" +
                        "  ?team wdt:P118 ?liga .\n" +
                        "  VALUES ?isteam { wd:Q476028 wd:Q20639856 wd:Q103229495}\n" +
                        "  ?team wdt:P31 ?isteam.\n" +
                        "  ?team rdfs:label ?name .\n" +
                        "  ?team wdt:P154 ?image_. \n" +
                        "  FILTER(LANG(?name) = \"en\")\n" +
                        "} GROUP BY ?name \n" +
                        "LIMIT 100";
    }

    @Override
    protected QuestionCategory getQuestionCategory() {
        return QuestionCategory.SPORT;
    }

    @Override
    protected AnswerCategory getAnswerCategory() {
        return AnswerCategory.SPORT_TEAM_LOGO;
    }

    @Override
    protected String getAnswerLabel() {
        return "name";
    }

    @Override
    public boolean needToSkip(String... parameters) {
        return false;
    }
}
