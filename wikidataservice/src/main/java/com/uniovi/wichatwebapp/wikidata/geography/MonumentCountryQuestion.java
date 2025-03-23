package com.uniovi.wichatwebapp.wikidata.geography;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.AnswerCategory;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MonumentCountryQuestion extends QuestionWikidata {
    private static final String[] spanishStringsIni = {"¿A qué país pertenece este monumento? ", "¿De qué país es este monumento? ", "¿Cuál es el país de este monumento? "};
    private static final String[] englishStringsIni = {"Which country does this monument belong to? ", "To which country belongs this monument? ", "From which country is this monument? ", "What is the country of this monument? "};

    private List<String> monumentLabels;

    public MonumentCountryQuestion(String langCode) {
        super(langCode);
    }

    @Override
    public void setQuery() {
        this.sparqlQuery = "SELECT DISTINCT ?monument ?monumentLabel ?image ?countryLabel " +
                "WHERE { " +
                "  ?monument wdt:P31 wd:Q4989906. " + // Filters for entities that are monuments
                "  ?monument wdt:P18 ?image. " + // Retrieves the image of the monument
                "  ?monument wdt:P17 ?country. " + // Retrieves the country where the monument is located
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"" + langCode + "\". } " + // Retrieves multilingual labels
                "} " +
                "LIMIT 150 ";
    }

    @Override
    public void processResults() {
        monumentLabels = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String monumentLabel = result.getJSONObject("monumentLabel").getString("value");
            String countryLabel = result.getJSONObject("countryLabel").getString("value");
            String image = result.has("image") ? result.getJSONObject("image").getString("value") : null; // Retrieves image if available

            if (needToSkip(monumentLabel, countryLabel)) {
                continue;
            }

            Answer answer = new Answer(countryLabel, AnswerCategory.MONUMENT_COUNTRY, langCode);
            answers.add(answer);

            String questionString;
            if (langCode.equals("es")) {
                questionString = spanishStringsIni[i % spanishStringsIni.length] + monumentLabel;
            } else {
                questionString = englishStringsIni[i % englishStringsIni.length] + monumentLabel;
            }

            if (image == null) {
                image = QuestionWikidata.DEFAULT_QUESTION_IMG; // Set default image if none available
            }

            Question question = new Question(answer, questionString, monumentLabel, QuestionCategory.GEOGRAPHY);
            question.setImageUrl(image); // Sets the monument's image
            questions.add(question);
        }

        qs.addAll(questions);
        as.addAll(answers);
    }

    private boolean needToSkip(String monumentLabel, String countryLabel) {
        if (monumentLabels.contains(monumentLabel)) {
            return true; // Avoid duplicate questions for the same monument
        }
        monumentLabels.add(monumentLabel);

        if (WikidataUtils.isEntityName(monumentLabel) || WikidataUtils.isEntityName(countryLabel)) {
            return true; // Skip if either name is invalid
        }

        return false;
    }

}
