package com.uniovi.wichatwebapp.wikidata.biology;


import com.uniovi.wichatwebapp.wikidata.ComposeQuestion;
import entities.AnswerCategory;
import entities.QuestionCategory;

public class AnimalScientificName extends ComposeQuestion {

    public AnimalScientificName(String langCode) {
        super(langCode);
    }

    @Override
    protected void initStringsIni() {
        spanishStringsIni = new String[]{"¿Cuál es el nombre científico de %s?"};
        englishStringsIni = new String[]{"What is the scientific name of %s?"};
    }

    // For testing
    public AnimalScientificName() {
        super();
    }

    @Override
    public void setQuery() {
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
    protected QuestionCategory getQuestionCategory() {
        return QuestionCategory.BIOLOGY;
    }

    @Override
    protected String getQuestionLabel() {
        return "commonName";
    }

    @Override
    protected AnswerCategory getAnswerCategory() {
        return AnswerCategory.ANIMAL_SCIENTIFIC_NAME;
    }

    @Override
    protected String getAnswerLabel() {
        return "scientificName";
    }


}