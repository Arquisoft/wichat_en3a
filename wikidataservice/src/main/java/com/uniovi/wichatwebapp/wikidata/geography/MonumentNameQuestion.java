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

public class MonumentNameQuestion extends QuestionWikidata {
    private static final String[] spanishStringsIni = {"¿Cómo se llama este monumento? ", "¿Qué nombre tiene este monumento? ", "¿Cuál es el nombre de este monumento? "};
    private static final String[] englishStringsIni = {"What is the name of this monument? ", "What do we call this monument? ", "Which name does this monument have? "};

    private List<String> monumentLabels;

    public MonumentNameQuestion(String langCode) {
        super(langCode);
    }

    @Override
    public void setQuery() {
        this.sparqlQuery = "SELECT DISTINCT ?monument ?monumentLabel ?image " +
                "WHERE { " +
                "  ?monument wdt:P31 wd:Q4989906. " + // Filters for entities that are monuments
                "  ?monument wdt:P18 ?image. " + // Retrieves the image of the monument
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
            String image = result.has("image") ? result.getJSONObject("image").getString("value") : null;

            if (needToSkip(monumentLabel)) {
                continue;
            }

            // Create the correct answer (monument's name)
            Answer answer = new Answer(monumentLabel, AnswerCategory.MONUMENT_NAME, langCode);
            answers.add(answer);

            String questionString;
            if (langCode.equals("es")) {
                questionString = spanishStringsIni[i % spanishStringsIni.length];
            } else {
                questionString = englishStringsIni[i % englishStringsIni.length];
            }

            if (image == null) {
                image = QuestionWikidata.DEFAULT_QUESTION_IMG; // Use default image if none available
            }

            // Create a new question
            Question question = new Question(answer, questionString, monumentLabel, QuestionCategory.GEOGRAPHY);
            question.setImageUrl(image); // Add image to the question
            questions.add(question);
        }

        qs.addAll(questions);
        as.addAll(answers);
    }

    private boolean needToSkip(String monumentLabel) {
        if (monumentLabels.contains(monumentLabel)) {
            return true; // Avoid duplicates
        }
        monumentLabels.add(monumentLabel);

        if (WikidataUtils.isEntityName(monumentLabel)) {
            return true; // Skip invalid entries
        }

        return false;
    }

    public List<Question> getQs() {
        return qs;
    }

    public List<Answer> getAs() {
        return as;
    }
}
