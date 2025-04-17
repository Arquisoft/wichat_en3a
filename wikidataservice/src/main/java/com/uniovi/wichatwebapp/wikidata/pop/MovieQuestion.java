package com.uniovi.wichatwebapp.wikidata.pop;


import com.uniovi.wichatwebapp.wikidata.ComposeQuestion;
import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
import entities.AnswerCategory;
import entities.QuestionCategory;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieQuestion extends ComposeQuestion {

    public MovieQuestion(String langCode) {
        super(langCode);
    }

    @Override
    protected void initStringsIni() {
        spanishStringsIni = new String[]{"¿En qué año salió %s?", "¿Cuándo salió %s?", "¿Qué año fue el estreno de %s?"};
        englishStringsIni = new String[]{"In what year did %s come out?", "When did %s come out?", "Which year was %s released in?"};
    }

    @Override
    protected String getQuestionLabel() {
        return "movieLabel";
    }

    // For testing
    public MovieQuestion() {
        super();
    }

    @Override
    public void setQuery() {
        this.sparqlQuery = "SELECT DISTINCT ?movieLabel ?poster (YEAR(?releaseDate) AS ?releaseYear) " +
                "WHERE { " +
                "  ?movie wdt:P31 wd:Q11424; " + // Filters for movies
                "         wdt:P577 ?releaseDate; " + // Retrieves the release date
                "         wdt:P495 wd:Q30. " + // Filters for U.S. movies (United States: Q30)
                "  OPTIONAL { ?movie wdt:P18 ?poster. } " + // Retrieves the poster image if available
                "  ?movie wdt:P166 ?award. " + // Filters for movies with awards (famous movies)
                "  FILTER(YEAR(?releaseDate) >= 2005 && YEAR(?releaseDate) <= 2025) " + // Filters for movies from 2005 to 2025
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"" + langCode + "\". } " + // Retrieves multilingual labels
                "} " +
                "ORDER BY RAND() " + // Randomizes the order for a scattered selection
                "LIMIT 100 ";
    }

    @Override
    protected QuestionCategory getQuestionCategory() {
        return QuestionCategory.POP_CULTURE;
    }

    @Override
    protected AnswerCategory getAnswerCategory() {
        return AnswerCategory.MOVIE_YEAR;
    }

    @Override
    protected String getAnswerLabel() {
        return "releaseYear";
    }

}