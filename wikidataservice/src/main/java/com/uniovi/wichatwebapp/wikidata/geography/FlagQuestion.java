package com.uniovi.wichatwebapp.wikidata.geography;


import com.uniovi.wichatwebapp.wikidata.SimpleQuestion;
import entities.AnswerCategory;
import entities.QuestionCategory;


public class FlagQuestion extends SimpleQuestion {

    public FlagQuestion(String langCode) {
        super(langCode);
    }

    @Override
    protected void initStringsIni() {
        spanishStringsIni = new String[]{"¿Que país tiene esta bandera? ", "¿A qué país pertenece esta bandera? ", "¿De qué país es esta bandera? ", "¿Cuál es el país de esta bandera? "};
        englishStringsIni= new String[]{"Which country has this flag? ", "To which country belongs this flag? ", "From which country is this flag? ", "What is the country represented by this flag? "};

    }

    //For testing
    public FlagQuestion() {
        super();
    }
    @Override
    public void setQuery() {
        this.sparqlQuery = "SELECT ?countryLabel ?image " +
                "WHERE " +
                "{ " +
                "  ?country wdt:P31 wd:Q6256; " + // Filters for countries
                "           wdt:P41 ?flag. " +// Retrieves flags
                " BIND(?flag AS ?image)"+
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"" + langCode + "\". } " + // Retrieves multilingual labels
                "}" +
                "LIMIT 100"; // Limits the results to 50
    }

    @Override
    protected QuestionCategory getQuestionCategory() {
        return QuestionCategory.GEOGRAPHY;
    }

    @Override
    protected AnswerCategory getAnswerCategory() {
        return AnswerCategory.FLAG;
    }

    @Override
    protected String getAnswerLabel() {
        return "countryLabel";
    }

}
