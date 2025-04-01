package com.uniovi.wichatwebapp.wikidata.pop;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.AnswerCategory;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BrandQuestion extends QuestionWikidata {
    private static final String[] spanishStringsIni = {"¿Qué marca tiene este logo? ", "¿De qué marca es este logo?", "¿A qué marca pertenece este logo?"};
    private static final String[] englishStringsIni = {"What brand has this logo? ", "Which brand does this logo belong to?", "Whose brand logo is this?"};
    private List<String> brandLabels = new ArrayList<>();

    public BrandQuestion(String langCode) {
        super(langCode);
    }
    //For testing
    public BrandQuestion(){
        super();
    }

    @Override
    public void setQuery() {
        this.sparqlQuery = "SELECT DISTINCT ?brand ?brandLabel ?logo " +
                "WHERE { " +
                "  ?brand wdt:P31 wd:Q431289. " + // Filters for entities that are brands
                "  ?brand wdt:P154 ?logo. " + // Ensures only brands with logos are included
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"" + langCode + "\". } " + // Retrieves multilingual labels
                "} " +
                "LIMIT 100 ";
    }

    @Override
    public void processResults() {
        brandLabels = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String brandLabel = result.getJSONObject("brandLabel").getString("value");
            String logo = result.has("logo") ? result.getJSONObject("logo").getString("value") : null;

            if (brandLabels.contains(brandLabel)) {
                continue; // Skip duplicate brands
            }
            brandLabels.add(brandLabel);

            Answer answer = new Answer(brandLabel, AnswerCategory.BRAND, langCode);
            answers.add(answer);

            String questionString;
            if (langCode.equals("es")) {
                questionString = spanishStringsIni[0]; // Use Spanish question template
            } else {
                questionString = englishStringsIni[0]; // Use English question template
            }

            if (logo == null) {
                logo = QuestionWikidata.DEFAULT_QUESTION_IMG;
            }
            Question question = new Question(answer, questionString, brandLabel, QuestionCategory.POP_CULTURE);
            question.setImageUrl(logo); // Set the logo image in the question
            questions.add(question);
        }

        qs.addAll(questions);
        as.addAll(answers);
    }
    //For testing
    List<String> getBrandLabels() {
        return brandLabels;
    }
}
