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

public class BasketballTeam extends QuestionWikidata {
    private static final String[] spanishStringsIni = {"¿En qué equipo está este deportista? ", "¿Cuál es el equipo de este deportista? "};
    private static final String[] englishStringsIni= {"What team is this person in? ", "What's this person's team? "};

    private List<String> athleteLabels;


    public BasketballTeam(String langCode) {
        super(langCode);
    }

    @Override
    protected void setQuery() {
        this.sparqlQuery =
                "SELECT DISTINCT ?name ?team_name ?image WHERE {   \n" +
                        "   VALUES ?occupation { wd:Q3665646 }\n" +
                        "  \n" +
                        "   ?person wdt:P106 ?occupation .   \n" +
                        "   ?person wikibase:sitelinks ?sitelinks .\n" +
                        "   FILTER(?sitelinks > 40)\n" +
                        "\n" +
                        "   ?person rdfs:label ?name .   \n" +
                        "   FILTER (LANG(?name) = \"en\")   \n" +
                        "\n" +
                        "   OPTIONAL { ?person wdt:P18 ?image. }    \n" +
                        "\n" +
                        "   ?person p:P54 ?teamMembership .   \n" +
                        "   ?teamMembership ps:P54 ?team .   \n" +
                        "   ?team rdfs:label ?team_name .   \n" +
                        "   FILTER (LANG(?team_name) = \"en\")   \n" +
                        "\n" +
                        "   ?teamMembership pq:P580 ?startDate .   \n" +
                        "   FILTER NOT EXISTS { ?teamMembership pq:P582 ?endDate }   \n" +
                        "\n" +
                        "   \n" +
                        "}    \n" +
                        "ORDER BY DESC(?sitelinks)   \n" +
                        "LIMIT 100";
    }

    @Override
    protected void processResults() {
        athleteLabels = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String athleteLabel = result.getJSONObject("name").getString("value");
            String teamLabel = result.getJSONObject("team_name").getString("value");
            String image = result.has("image") ? result.getJSONObject("image").getString("value") : null; // Retrieves image if available

            if (needToSkip(athleteLabel, teamLabel)) {
                continue;
            }

            Answer answer = new Answer(teamLabel, AnswerCategory.PERSON_BASKETBALL_TEAM, langCode);
            answers.add(answer);

            String questionString;
            if (langCode.equals("es")) {
                questionString = spanishStringsIni[i % spanishStringsIni.length] + athleteLabel;
            } else {
                questionString = englishStringsIni[i % englishStringsIni.length] + athleteLabel;
            }

            if (image == null) {
                image = QuestionWikidata.DEFAULT_QUESTION_IMG; // Set default image if none available
            }

            Question question = new Question(answer, questionString, athleteLabel, QuestionCategory.SPORT);
            question.setImageUrl(image); // Sets the monument's image
            questions.add(question);
        }

        qs.addAll(questions);
        as.addAll(answers);
    }

    private boolean needToSkip(String athleteLabel, String teamLabel) {
        if (athleteLabels.contains(athleteLabel)) {
            return true; // Avoid duplicate questions for the same monument
        }
        athleteLabels.add(athleteLabel);

        if (WikidataUtils.isEntityName(athleteLabel) || WikidataUtils.isEntityName(teamLabel)) {
            return true; // Skip if either name is invalid
        }

        return false;
    }


}
