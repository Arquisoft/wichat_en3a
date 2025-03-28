package com.uniovi.wichatwebapp.wikidata.pop;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.AnswerCategory;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieQuestion extends QuestionWikidata {
    private static final String[] spanishStringsIni = {"¿En qué año salió esta película? ", "¿Cuándo salió esta película? ", "¿Qué año fue el estreno de esta película? "};
    private static final String[] englishStringsIni = {"In what year did this movie come out? ", "When did this movie come out? ", "Which year was this movie released in? "};

    private List<String> movieLabels;

    public MovieQuestion(String langCode) {
        super(langCode);
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

            if (movieLabels.contains(movieLabel)) {
                continue; // Skip duplicate movies
            }
            movieLabels.add(movieLabel);

            Answer answer = new Answer(releaseYear, AnswerCategory.MOVIE_YEAR, langCode);
            answers.add(answer);
            String questionString;

            if (langCode.equals("es")) {
                questionString = spanishStringsIni[i % 3] + movieLabel;
            } else {
                questionString = englishStringsIni[i % 3] + movieLabel;
            }
            if(poster==null){
                poster = QuestionWikidata.DEFAULT_QUESTION_IMG;
            }
            Question question = new Question(answer, questionString, movieLabel, QuestionCategory.POP_CULTURE);
            question.setImageUrl(poster); // Set the poster image in the question
            questions.add(question);
        }

        qs.addAll(questions);
        as.addAll(answers);
    }

}
