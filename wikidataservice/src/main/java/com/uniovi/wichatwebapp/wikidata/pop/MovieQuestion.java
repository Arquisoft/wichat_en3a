package com.uniovi.wichatwebapp.wikidata.pop;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.AnswerCategory;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieQuestion extends QuestionWikidata {
    private static final String[] spanishStringsIni = {"¿En qué año salió %s?", "¿Cuándo salió %s?", "¿Qué año fue el estreno de %s?"};
    private static final String[] englishStringsIni = {"In what year did %s come out?", "When did %s come out?", "Which year was %s released in?"};

    private List<String> movieLabels = new ArrayList<>();

    public MovieQuestion(String langCode) {
        super(langCode);
    }

    // For testing
    public MovieQuestion() {
        super();
    }

    @Override
    public void setQuery() {
        this.sparqlQuery = "SELECT DISTINCT ?movie ?movieLabel ?poster (YEAR(?releaseDate) AS ?releaseYear) " +
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
    public void processResults() {
        movieLabels = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String movieLabel = result.getJSONObject("movieLabel").getString("value");
            String releaseYear = result.getJSONObject("releaseYear").getString("value");
            String poster = result.has("poster") ? result.getJSONObject("poster").getString("value") : null; // Retrieve the poster URL if available

            if (needToSkip(movieLabel)) {
                continue; // Skip duplicate movies
            }
            movieLabels.add(movieLabel);

            Answer answer = new Answer(releaseYear, AnswerCategory.MOVIE_YEAR, langCode);
            answers.add(answer);
            String questionString;

            if (langCode.equals("es")) {
                questionString = String.format(spanishStringsIni[i % spanishStringsIni.length], movieLabel);
            } else {
                questionString = String.format(englishStringsIni[i % englishStringsIni.length], movieLabel);
            }
            if (poster == null) {
                poster = QuestionWikidata.DEFAULT_QUESTION_IMG;
            }
            Question question = new Question(answer, questionString, movieLabel, QuestionCategory.POP_CULTURE);
            question.setImageUrl(poster); // Set the poster image in the question
            questions.add(question);
        }

        qs.addAll(questions);
        as.addAll(answers);
    }

    @Override
    protected boolean needToSkip(String... parameters) {
        if (movieLabels.contains(parameters[0])) {
            return true; // Avoid duplicates
        }
        movieLabels.add(parameters[0]);

        if (WikidataUtils.isEntityName(parameters[0])) {
            return true; // Skip invalid entries
        }

        return false;
    }

    // For testing
    List<String> getMovieLabels() {
        return movieLabels;
    }
}