package com.uniovi.wichatwebapp.wikidata;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Question;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FlagQuestion extends QuestionWikidata{
    private static final String[] spanishStringsIni = {"¿Que país tiene esta bandera? ", "¿A qué país pertenece esta bandera? ", "¿De qué país es esta bandera? ", "¿Cuál es el país de esta bandera? "};
    private static final String[] englishStringsIni= {"Which country has this flag? ", "To which country belongs this flag? ", "From which country is this flag? ", "What is the country represented by this flag? "};

    List<String> countryLabels;



    public FlagQuestion(String langCode) {
        super(langCode);
    }

    @Override
    public void setQuery() {
        this.sparqlQuery = "SELECT ?countryLabel ?flagLabel\n" +
                "WHERE " +
                "{ " +
                "  ?country wdt:P31 wd:Q6256; " +
                "           wdt:P41 ?flag. " +
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"" + langCode + "\". } " +
                "}";
    }

    @Override
    public void processResults() {
        countryLabels = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String countryLabel = result.getJSONObject("countryLabel").getString("value");
            String flagLabel = result.getJSONObject("flagLabel").getString("value");

            if (needToSkip(countryLabel, flagLabel)) {
                continue;
            }

            Answer a = new Answer(countryLabel, langCode);
            answers.add(a);
            String questionString;

            if (langCode.equals("es")){
                questionString = spanishStringsIni[i%4] /*+ WikidataUtils.LINKCONCAT + flagLabel*/;

            } else {
                questionString = englishStringsIni[i%4] /*+ WikidataUtils.LINKCONCAT + flagLabel*/;
            }

            questions.add(new Question(a, questionString, flagLabel));
        }
        //questionService.saveAllAnswers(answers);
        //questionService.saveAllQuestions(questions);

        qs.addAll(questions);
        as.addAll(answers);
    }

    private boolean needToSkip(String countryLabel, String venueLabel){
        if (countryLabels.contains(countryLabel)) {
            return true;
        }
        countryLabels.add(countryLabel);

        if (WikidataUtils.isEntityName(countryLabel) || WikidataUtils.isEntityName(venueLabel)) {
            return true;
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
