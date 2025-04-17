package com.uniovi.wichatwebapp.wikidata;

/**
 * Class for questions where you have the base text and something else.
 * Like "What is the lifespan of a Dog?"
 * The "Dog" part is the questionLabel
 */
public abstract class ComposeQuestion extends QuestionWikidata {

    public ComposeQuestion() {
        super();
    }

    public ComposeQuestion(String langCode) {
        super(langCode);
    }

    @Override
    protected String getSkipLabel(int i){
        return results.getJSONObject(i).getJSONObject(getQuestionLabel()).getString("value");
    }

    @Override
    protected String createQuestionString(int i) {

        String questionLabel = results.getJSONObject(i).getJSONObject(getQuestionLabel()).getString("value");

        if(langCode.equals("es")){
            return String.format(spanishStringsIni[i % spanishStringsIni.length], questionLabel);
        }else{
            return String.format(englishStringsIni[i % englishStringsIni.length], questionLabel);
        }
    }

    protected abstract String getQuestionLabel();
}
