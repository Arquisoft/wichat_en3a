package com.uniovi.wichatwebapp.wikidata.sports;


import com.uniovi.wichatwebapp.wikidata.ComposeQuestion;
import entities.AnswerCategory;
import entities.QuestionCategory;

public class FootballTeam extends ComposeQuestion {

    public FootballTeam(String langCode) {
        super(langCode);
    }

    @Override
    protected void initStringsIni() {
        spanishStringsIni = new String[]{"¿En qué equipo está %s?", "¿Cuál es el equipo de %s?"};
        englishStringsIni = new String[]{"What team is %s in?", "What's %s's team?"};
    }

    @Override
    protected String getQuestionLabel() {
        return "name";
    }

    // For testing
    public FootballTeam() {
        super();
    }

    @Override
    public void setQuery() {
        this.sparqlQuery =
                "SELECT DISTINCT ?name ?team_name ?image WHERE {   \n" +
                        "   VALUES ?occupation { wd:Q937857  }\n" +
                        "  \n" +
                        "   ?person wdt:P106 ?occupation .   \n" +
                        "   ?person wikibase:sitelinks ?sitelinks .\n" +
                        "   FILTER(?sitelinks > 40)\n" +
                        "\n" +
                        "   ?person rdfs:label ?name .   \n" +
                        "   FILTER (LANG(?name) = \"en\")   \n" +
                        "\n" +
                        "   OPTIONAL { ?person wdt:P18 ?image. }    \n" +
                        "\n" +
                        "   ?person p:P54 ?teamMembership .   \n" +
                        "   ?teamMembership ps:P54 ?team .   \n" +
                        "   ?team rdfs:label ?team_name .   \n" +
                        "   FILTER (LANG(?team_name) = \"en\")   \n" +
                        "\n" +
                        "   ?teamMembership pq:P580 ?startDate .   \n" +
                        "   FILTER NOT EXISTS { ?teamMembership pq:P582 ?endDate }   \n" +
                        "\n" +
                        "   \n" +
                        "}    \n" +
                        "ORDER BY DESC(?sitelinks)   \n" +
                        "LIMIT 100";
    }

    @Override
    protected QuestionCategory getQuestionCategory() {
        return QuestionCategory.SPORT;
    }

    @Override
    protected AnswerCategory getAnswerCategory() {
        return AnswerCategory.PERSON_FOOTBALL_TEAM;
    }

    @Override
    protected String getAnswerLabel() {
        return "team_name";
    }

}