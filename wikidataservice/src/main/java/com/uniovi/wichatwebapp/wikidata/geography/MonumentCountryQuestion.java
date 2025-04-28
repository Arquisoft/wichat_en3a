package com.uniovi.wichatwebapp.wikidata.geography;


import com.uniovi.wichatwebapp.wikidata.ComposeQuestion;
import entities.AnswerCategory;
import entities.QuestionCategory;

public class MonumentCountryQuestion extends ComposeQuestion {

    public MonumentCountryQuestion(String langCode) {
        super(langCode);
    }

    @Override
    protected void initStringsIni() {
        spanishStringsIni = new String[]{"¿A qué país pertenece %s?", "¿De qué país es %s?", "¿Cuál es el país de %s?"};
        englishStringsIni = new String[]{"Which country does %s belong to?", "To which country belongs %s?", "From which country is %s?", "What is the country of %s?"};
    }


    // For testing
    public MonumentCountryQuestion() {
        super();
    }

    @Override
    public void setQuery() {
        this.sparqlQuery = "SELECT DISTINCT ?monumentLabel ?image ?countryLabel " +
                "WHERE { " +
                "  ?monument wdt:P31 wd:Q4989906. " + // Filters for entities that are monuments
                "  ?monument wdt:P18 ?image. " + // Retrieves the image of the monument
                "  ?monument wdt:P17 ?country. " + // Retrieves the country where the monument is located
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"" + langCode + "\". } " + // Retrieves multilingual labels
                "} " +
                "LIMIT 100 ";
    }

    @Override
    protected QuestionCategory getQuestionCategory() {
        return QuestionCategory.GEOGRAPHY;
    }

    @Override
    protected AnswerCategory getAnswerCategory() {
        return AnswerCategory.MONUMENT_COUNTRY;
    }

    @Override
    protected String getAnswerLabel() {
        return "countryLabel";
    }

    @Override
    protected String getQuestionLabel() {
        return "monumentLabel";
    }

}