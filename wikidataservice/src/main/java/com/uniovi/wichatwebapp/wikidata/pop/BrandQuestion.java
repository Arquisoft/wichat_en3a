package com.uniovi.wichatwebapp.wikidata.pop;


import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.SimpleQuestion;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
import entities.AnswerCategory;
import entities.QuestionCategory;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BrandQuestion extends SimpleQuestion {

    public BrandQuestion(String langCode) {
        super(langCode);
    }

    @Override
    protected void initStringsIni() {
        spanishStringsIni = new String[]{"¿Qué marca tiene este logo? ", "¿De qué marca es este logo?", "¿A qué marca pertenece este logo?"};
        englishStringsIni = new String[]{"What brand has this logo? ", "Which brand does this logo belong to?", "Whose brand logo is this?"};
    }

    //For testing
    public BrandQuestion(){
        super();
    }

    @Override
    public void setQuery() {
        this.sparqlQuery = "SELECT DISTINCT ?brand ?brandLabel  ?image " +
                "WHERE { " +
                "  ?brand wdt:P31 wd:Q431289. " + // Filters for entities that are brands
                "  ?brand wdt:P154 ?logo. " + // Ensures only brands with logos are included
                " BIND(?logo AS ?image) "+
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"" + langCode + "\". } " + // Retrieves multilingual labels
                "} " +
                "LIMIT 100 ";
    }

    @Override
    protected QuestionCategory getQuestionCategory() {
        return QuestionCategory.POP_CULTURE;
    }

    @Override
    protected AnswerCategory getAnswerCategory() {
        return AnswerCategory.BRAND;
    }

    @Override
    protected String getAnswerLabel() {
        return "brandLabel";
    }

}
