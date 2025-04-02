package com.uniovi.wichatwebapp.wikidata.geography;

import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
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
    void needToSkipTest_DetectsDuplicateMonumentLabels() {
        assertFalse(monumentCountryQuestion.getMonumentLabels().contains("Eiffel Tower"), "First check: should not contain Eiffel Tower.");
        monumentCountryQuestion.getMonumentLabels().add("Eiffel Tower"); // Manually add for test
        assertTrue(monumentCountryQuestion.getMonumentLabels().contains("Eiffel Tower"), "Second check: should now contain Eiffel Tower.");
        assertTrue(monumentCountryQuestion.needToSkip("Eiffel Tower", "France"), "Should skip duplicate monument label.");
    }
    @Test
    void needToSkipTest_SkipsInvalidEntityNames() {
        assertTrue(WikidataUtils.isEntityName("Q12345"), "Q12345 should be recognized as an entity name.");
        assertTrue(monumentCountryQuestion.needToSkip("Q12345", "France"), "Should skip invalid monument name.");
    }
    @Test
    void needToSkipTest_ProcessesValidEntriesCorrectly() {
        assertFalse(monumentCountryQuestion.needToSkip("Statue of Liberty", "United States"), "Valid monument name should not be skipped.");
        assertFalse(monumentCountryQuestion.needToSkip("Colosseum", "Italy"), "Valid monument name should not be skipped.");
    }

}
