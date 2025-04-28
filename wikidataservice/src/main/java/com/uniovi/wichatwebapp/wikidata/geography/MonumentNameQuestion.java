package com.uniovi.wichatwebapp.wikidata.geography;


import com.uniovi.wichatwebapp.wikidata.SimpleQuestion;
import entities.AnswerCategory;
import entities.QuestionCategory;

public class MonumentNameQuestion extends SimpleQuestion {

    public MonumentNameQuestion(String langCode) {
        super(langCode);
    }

    @Override
    protected void initStringsIni() {
        spanishStringsIni = new String[]{"¿Cómo se llama este monumento? ", "¿Qué nombre tiene este monumento? ", "¿Cuál es el nombre de este monumento? "};
        englishStringsIni = new String[]{"What is the name of this monument? ", "What do we call this monument? ", "Which name does this monument have? "};
    }

    //For testing
    public MonumentNameQuestion(){
        super();
    }
    @Override
    public void setQuery() {
        this.sparqlQuery = "SELECT DISTINCT ?monumentLabel ?image " +
                "WHERE { " +
                "  ?monument wdt:P31 wd:Q4989906. " + // Filters for entities that are monuments
                "  ?monument wdt:P18 ?image. " + // Retrieves the image of the monument
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
        return AnswerCategory.MONUMENT_NAME;
    }

    @Override
    protected String getAnswerLabel() {
        return "monumentLabel";
    }


}
