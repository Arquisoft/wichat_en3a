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
    private static final String[] spanishStringsIni = {"¿A qué país pertenece %s?", "¿De qué país es %s?", "¿Cuál es el país de %s?"};
    private static final String[] englishStringsIni = {"Which country does %s belong to?", "To which country belongs %s?", "From which country is %s?", "What is the country of %s?"};

    private List<String> monumentLabels = new ArrayList<>();

    public MonumentCountryQuestion(String langCode) {
        super(langCode);
    }

    // For testing
    public MonumentCountryQuestion() {
        super();
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
                "LIMIT 100 ";
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
                questionString = String.format(spanishStringsIni[i % spanishStringsIni.length], monumentLabel);
            } else {
                questionString = String.format(englishStringsIni[i % englishStringsIni.length], monumentLabel);
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

    @Override
    protected boolean needToSkip(String... parameters) {
        if (monumentLabels.contains(parameters[0])) {
            return true; // Avoid duplicate questions for the same monument
        }
        monumentLabels.add(parameters[0]);

        if (WikidataUtils.isEntityName(parameters[0]) || WikidataUtils.isEntityName(parameters[1])) {
            return true; // Skip if either name is invalid
        }

        return false;
    }

    // For testing
    public List<String> getMonumentLabels() {
        return monumentLabels;
    }
}