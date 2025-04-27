package com.uniovi.wichatwebapp.wikidata;


import entities.Answer;
import entities.AnswerCategory;
import entities.Question;
import entities.QuestionCategory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class QuestionWikidata {
    public final static String DEFAULT_QUESTION_IMG ="https://cdn.pixabay.com/photo/2015/11/03/08/56/question-mark-1019820_1280.jpg";
    // Query to be sent to WikiData QS
    protected String sparqlQuery;
    // Response given by WikiData QS for the query sent
    protected JSONArray results;

    public void setResults(JSONArray results) {
        this.results = results;
    }

    public String getSparqlQuery() {
        return sparqlQuery;
    }

    public JSONArray getResults() {
        return results;
    }

    // Language code representing in what language the query must be sent. Spanish as a default value.
    protected String langCode = "es";

    protected static String[] spanishStringsIni;
    protected static String[] englishStringsIni;

    //List with the final questions
    protected List<Question> qs = new ArrayList<>();
    //List with the final answers
    protected List<Answer> as = new ArrayList<>();

    protected List<String> answerLabels = new ArrayList<>();

    /**
     * Constructor for QuestionTemplates which is also the one in charge of the whole question retrieval process for a query
     * For future types of question, only need to override abstract methods and calling super() on constructor
     * When instancing a question, only constructor invocation is required.
     * For reference in future implementations: look at CountryCapitalQuestion
     */
    public QuestionWikidata(String langCode) {
        try {
            this.langCode = langCode;
            initStringsIni();
            setQuery();
            call();
            processResults();
        } catch (Exception e) {
            System.err.println("Error while processing the question: " + e.getMessage());
        }
    }

    protected abstract void initStringsIni();

    //For testing
    public QuestionWikidata(){
        initStringsIni();
        setQuery();
    }
    /**
     * Update the value of @sparqlQuery with the query to be sent.
     */
    public abstract void setQuery();

    /**
     * Method for the whole processing the @results given by WikiData QS as a JSON.
     * It also is in charge of storing both the processed answers and the question in all languages
     */
    public void processResults(){
        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String answerLabel = result.getJSONObject(getAnswerLabel()).getString("value");
            answerLabel = WikidataUtils.capitalize(answerLabel);
            String image = result.has("image") ? result.getJSONObject("image").getString("value") : DEFAULT_QUESTION_IMG;// Retrieves image if available

            if(needToSkip(getSkipLabel(i))){
                continue;
            }

            Answer answer = new Answer(answerLabel, getAnswerCategory(), langCode);
            answers.add(answer);

            String questionString = createQuestionString(i);

            Question question = new Question(answer, questionString, image, getQuestionCategory());
            questions.add(question);
        }

        qs.addAll(questions);
        as.addAll(answers);
    }

    protected abstract String getSkipLabel(int i);

    protected abstract QuestionCategory getQuestionCategory();

    protected abstract String createQuestionString(int i);

    protected abstract AnswerCategory getAnswerCategory();

    protected abstract String getAnswerLabel();

    /**
     * Method in charge of the HTTP request with WikiData QS.
     * It allows to send only one query, so it does not support questions whose answer require multiple queries.
     * CAUTION: Remember to update the results field of the field if this method gets overwritten.
     */
    protected void call() {
        // Set up the HTTP client
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://query.wikidata.org/sparql"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")  // Specify JSON format
                .POST(HttpRequest.BodyPublishers.ofString("query=" + URLEncoder.encode(sparqlQuery, StandardCharsets.UTF_8)))
                .build();

        // Send the HTTP request and get the response
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.err.println("Error: Received HTTP code " + response.statusCode());
                System.err.println("Response body: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to send the request", e);
        }


        JSONObject jsonResponse = new JSONObject(response.body());
        JSONArray results = jsonResponse.getJSONObject("results").getJSONArray("bindings");

        this.results = results; // Save the results. If this method is overwritten this line MUST be kept
    }

    public List<Question> getQs() {
        return qs;
    }

    public List<Answer> getAs() {
        return as;
    }

    public boolean needToSkip(String... parameters){
        if(answerLabels.contains(parameters[0])){
            return true;
        }
        answerLabels.add(parameters[0]);
        if(WikidataUtils.isEntityName(parameters[0])){
            return true;
        }
        return false;
    }

    public List<String> getAnswerLabels() {
        return answerLabels;
    }
}
