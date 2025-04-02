package com.uniovi.wichatwebapp.wikidata.geography;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



class MonumentCountryQuestionTest {
    private MonumentCountryQuestion monumentCountryQuestion;

    @BeforeEach
    void setUp() {
        monumentCountryQuestion = new MonumentCountryQuestion(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        monumentCountryQuestion.setQuery();
        assertNotNull(monumentCountryQuestion.getSparqlQuery());
        assertFalse(monumentCountryQuestion.getSparqlQuery().isEmpty());
    }

    @Test
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"monumentLabel\": {\"value\": \"Eiffel Tower\"},"
                + "\"countryLabel\": {\"value\": \"France\"},"
                + "\"image\": {\"value\": \"https://example.com/eiffel.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        monumentCountryQuestion.setResults(mockResults);

        // Process the results
        monumentCountryQuestion.processResults();

        // Verify that at least one question contains expected text
        String expectedQuestionText = "Eiffel";
        assertTrue(
                monumentCountryQuestion.getQs().stream().anyMatch(q -> q.getContent().contains(expectedQuestionText)),
                "Expected question text was not found in stored questions."
        );

        // Verify that at least one answer contains expected text
        String expectedAnswerText = "France";
        assertTrue(
                monumentCountryQuestion.getAs().stream().anyMatch(a -> a.getText().equals(expectedAnswerText)),
                "Expected answer text was not found in stored answers."
        );
    }

    @Test
    void needToSkipTest_Duplicates_ReturnTrue() {
        assertFalse(monumentCountryQuestion.needToSkip("Eiffel Tower", "France")); // First time: should be false
        assertTrue(monumentCountryQuestion.needToSkip("Eiffel Tower", "France")); // Second time: should be true
    }
}
