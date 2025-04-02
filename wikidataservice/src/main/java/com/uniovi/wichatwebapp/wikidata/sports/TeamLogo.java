package com.uniovi.wichatwebapp.wikidata.sports;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.AnswerCategory;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeamLogo extends QuestionWikidata {
    private static final String[] spanishStringsIni = {"¿De qué equipo es este escudo?", "¿Cuál es el equipo con este escudo?", "¿A qué equipo pertenece este escudo?"};
    private static final String[] englishStringsIni= {"What team is this logo from?", "Which team has this logo?", "To which team belongs this logo?"};

    public TeamLogo() {
        super();
    }


    public TeamLogo(String langCode) {
        super(langCode);
    }

    @Override
    protected void setQuery() {
        this.sparqlQuery =
                "SELECT DISTINCT ?name (MAX(?image_) AS ?image) WHERE {\n" +
                        "  VALUES ?liga { wd:Q324867 wd:Q35615 wd:Q9448 wd:Q15804 wd:Q82595 }\n" +
                        "  ?team wdt:P118 ?liga .\n" +
                        "  VALUES ?isteam { wd:Q476028 wd:Q20639856 wd:Q103229495}\n" +
                        "  ?team wdt:P31 ?isteam.\n" +
                        "  ?team rdfs:label ?name .\n" +
                        "  ?team wdt:P154 ?image_. \n" +
                        "  FILTER(LANG(?name) = \"en\")\n" +
                        "} GROUP BY ?name \n" +
                        "LIMIT 100";
    }

    @Override
    protected void processResults() {
        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String teamLabel = result.getJSONObject("name").getString("value");
            String image = result.has("image") ? result.getJSONObject("image").getString("value") : null; // Retrieves image if available

            Answer answer = new Answer(teamLabel, AnswerCategory.SPORT_TEAM_LOGO, langCode);
            answers.add(answer);

            String questionString;
            if (langCode.equals("es")) {
                questionString = spanishStringsIni[i % spanishStringsIni.length];
            } else {
                questionString = englishStringsIni[i % englishStringsIni.length];
            }

            if (image == null) {
                image = QuestionWikidata.DEFAULT_QUESTION_IMG; // Set default image if none available
            }

            Question question = new Question(answer, questionString, image, QuestionCategory.SPORT);
            question.setImageUrl(image); // Sets the monument's image
            questions.add(question);
        }

        qs.addAll(questions);
        as.addAll(answers);
    }

    @Override
    protected boolean needToSkip(String... parameters) {
        return false;
    }
}
