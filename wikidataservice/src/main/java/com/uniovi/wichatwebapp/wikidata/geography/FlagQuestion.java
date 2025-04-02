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


public class FlagQuestion extends QuestionWikidata {
    private static final String[] spanishStringsIni = {"¿Que país tiene esta bandera? ", "¿A qué país pertenece esta bandera? ", "¿De qué país es esta bandera? ", "¿Cuál es el país de esta bandera? "};
    private static final String[] englishStringsIni= {"Which country has this flag? ", "To which country belongs this flag? ", "From which country is this flag? ", "What is the country represented by this flag? "};

    List<String> countryLabels = new ArrayList<>();



    public FlagQuestion(String langCode) {
        super(langCode);
    }

    //For testing
    public FlagQuestion() {
        super();
    }
    @Override
    public void setQuery() {
        this.sparqlQuery = "SELECT ?countryLabel ?flagLabel\n" +
                "WHERE " +
                "{ " +
                "  ?country wdt:P31 wd:Q6256; " + // Filters for countries
                "           wdt:P41 ?flag. " + // Retrieves flags
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"" + langCode + "\". } " + // Retrieves multilingual labels
                "}" +
                "LIMIT 100"; // Limits the results to 50
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

            Answer a = new Answer(countryLabel, AnswerCategory.FLAG,langCode);
            answers.add(a);
            String questionString;

            if (langCode.equals("es")){
                questionString = spanishStringsIni[i%4] /*+ WikidataUtils.LINKCONCAT + flagLabel*/;

            } else {
                questionString = englishStringsIni[i%4] /*+ WikidataUtils.LINKCONCAT + flagLabel*/;
            }

            questions.add(new Question(a, questionString, flagLabel, QuestionCategory.GEOGRAPHY));
        }
        //questionService.saveAllAnswers(answers);
        //questionService.saveAllQuestions(questions);

        qs.addAll(questions);
        as.addAll(answers);
    }

    @Override
    protected boolean needToSkip(String... parameters) {
        if (countryLabels.contains(parameters[0])) {
            return true;
        }
        countryLabels.add(parameters[0]);

        if (WikidataUtils.isEntityName(parameters[0]) || WikidataUtils.isEntityName(parameters[1])) {
            return true;
        }

        return false;
    }

}
