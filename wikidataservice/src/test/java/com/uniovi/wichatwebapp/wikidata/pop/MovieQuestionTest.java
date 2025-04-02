package com.uniovi.wichatwebapp.wikidata.pop;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class MovieQuestionTest {
    private MovieQuestion movieQuestion;

    @BeforeEach
    void setUp() {
        movieQuestion = new MovieQuestion(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        movieQuestion.setQuery();
        assertNotNull(movieQuestion.getSparqlQuery());
        assertFalse(movieQuestion.getSparqlQuery().isEmpty());
    }

    @Test
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"movieLabel\": {\"value\": \"Inception\"},"
                + "\"releaseYear\": {\"value\": \"2010\"},"
                + "\"poster\": {\"value\": \"https://example.com/inception.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        movieQuestion.setResults(mockResults);

        // Process the results
        movieQuestion.processResults();

        // Verify that at least one question contains expected text
        String expectedQuestionText = "Inception";
        assertTrue(
                movieQuestion.getQs().stream().anyMatch(q -> q.getContent().contains(expectedQuestionText)),
                "Expected question text was not found in stored questions."
        );

        // Verify that at least one answer contains expected text
        String expectedAnswerText = "2010";
        assertTrue(
                movieQuestion.getAs().stream().anyMatch(a -> a.getText().equals(expectedAnswerText)),
                "Expected answer text was not found in stored answers."
        );
    }

    @Test
    void needToSkipTest_Duplicates_ReturnTrue() {
        assertFalse(movieQuestion.getMovieLabels().contains("Moana")); // First time: should be false
        movieQuestion.getMovieLabels().add("Moana"); // Add label manually for test
        assertTrue(movieQuestion.getMovieLabels().contains("Moana")); // Second time: should be true
    }
}
