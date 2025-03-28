package com.uniovi.wichatwebapp.wikidata.biology;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.AnswerCategory;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnimalScientificName extends QuestionWikidata {
    private static final String[] spanishStringsIni = {"¿Cuál es el nombre científico de este animal? "};
    private static final String[] englishStringsIni= {"What is the scientific name of this animal? "};

    private List<String> animalLabels;
    private List<String> animalScientificNames;


    public AnimalScientificName(String langCode) {
        super(langCode);
    }

    @Override
    protected void setQuery() {
        this.sparqlQuery =
                "SELECT DISTINCT ?commonName ?image ?scientificName WHERE {\n" +
                        "  ?animal wdt:P1034 ?foodSource .\n" +
                        "  ?animal wdt:P31 wd:Q16521 .\n" +
                        "  ?animal wdt:P225 ?scientificName .\n" +
                        "  ?animal wikibase:sitelinks ?sitelinks .\n" +
                        "  ?animal wdt:P18 ?image .\n" +
                        "  FILTER(?sitelinks < 100)\n" +
                        "  FILTER(?sitelinks > 50)\n" +
                        "  ?animal p:P1843 ?commonNameStatement .\n" +
                        "  ?commonNameStatement ps:P1843 ?commonName .\n" +
                        "  FILTER(LANG(?commonName) = 'en')\n" +
                        "}\n" +
                        "ORDER BY DESC(?sitelinks)\n" +
                        "LIMIT 100";
    }

    @Override
    protected void processResults() {
        animalLabels = new ArrayList<>();
        animalScientificNames = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String name = result.getJSONObject("commonName").getString("value");
            String scientificName = result.getJSONObject("scientificName").getString("value");
            name = WikidataUtils.capitalize(name);
            String image = result.has("image") ? result.getJSONObject("image").getString("value") : null; // Retrieves image if available

            if (needToSkip(name, scientificName)) {
                continue;
            }

            Answer answer = new Answer(scientificName, AnswerCategory.ANIMAL_SCIENTIFIC_NAME, langCode);
            answers.add(answer);

            String questionString;
            if (langCode.equals("es")) {
                questionString = spanishStringsIni[i % spanishStringsIni.length] + name;
            } else {
                questionString = englishStringsIni[i % englishStringsIni.length] + name;
            }

            if (image == null) {
                image = QuestionWikidata.DEFAULT_QUESTION_IMG; // Set default image if none available
            }

            Question question = new Question(answer, questionString, name, QuestionCategory.BIOLOGY);
            question.setImageUrl(image); // Sets the monument's image
            questions.add(question);
        }

        qs.addAll(questions);
        as.addAll(answers);
    }

    private boolean needToSkip(String name, String scientificName) {
        if (animalLabels.contains(name) || animalScientificNames.contains(scientificName)) {
            return true; // Avoid duplicate questions for the same monument
        }
        animalLabels.add(name);

        if (WikidataUtils.isEntityName(name) || WikidataUtils.isEntityName(scientificName)) {
            return true; // Skip if either name is invalid
        }

        return false;
    }


}
