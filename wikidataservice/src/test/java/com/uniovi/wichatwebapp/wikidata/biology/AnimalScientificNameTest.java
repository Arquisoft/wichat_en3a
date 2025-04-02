package com.uniovi.wichatwebapp.wikidata.biology;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class AnimalScientificNameTest {
    private AnimalScientificName animalScientificName;

    @BeforeEach
    void setUp() {
        animalScientificName = new AnimalScientificName(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        animalScientificName.setQuery();
        assertNotNull(animalScientificName.getSparqlQuery());
        assertFalse(animalScientificName.getSparqlQuery().isEmpty());
    }

    @Test
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"commonName\": {\"value\": \"Lion\"},"
                + "\"scientificName\": {\"value\": \"Panthera leo\"},"
                + "\"image\": {\"value\": \"https://example.com/lion.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        animalScientificName.setResults(mockResults);

        // Process the results
        animalScientificName.processResults();

        // Verify that at least one question contains expected text
        String expectedQuestionText = "Lion";
        assertTrue(
                animalScientificName.getQs().stream().anyMatch(q -> q.getContent().contains(expectedQuestionText)),
                "Expected question text was not found in stored questions."
        );

        // Verify that at least one answer contains expected text
        String expectedAnswerText = "Panthera leo";
        assertTrue(
                animalScientificName.getAs().stream().anyMatch(a -> a.getText().equals(expectedAnswerText)),
                "Expected answer text was not found in stored answers."
        );
    }

    @Test
    void needToSkipTest_Duplicates_ReturnTrue() {
        assertFalse(animalScientificName.needToSkip("Lion", "Panthera leo")); // First time: should be false
        assertTrue(animalScientificName.needToSkip("Lion", "Panthera leo")); // Second time: should be true
    }
}
