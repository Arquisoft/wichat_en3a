package com.uniovi.wichatwebapp.wikidata;

public abstract class SimpleQuestion extends QuestionWikidata {


    public SimpleQuestion() {
        super();
    }

    public SimpleQuestion(String langCode) {
        super(langCode);
    }

    @Override
    protected String getSkipLabel(int i){
        return results.getJSONObject(i).getJSONObject(getAnswerLabel()).getString("value");
    }

    @Override
    protected String createQuestionString(int i) {
        if(langCode.equals("es")){
            return spanishStringsIni[i%spanishStringsIni.length];
        }else{
            return englishStringsIni[i%englishStringsIni.length];
        }
    }
}
