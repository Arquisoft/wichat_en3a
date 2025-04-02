package com.uniovi.wichatwebapp.wikidata.biology;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class WhatAnimalTest {
    private WhatAnimal whatAnimal;

    @BeforeEach
    void setUp() {
        whatAnimal = new WhatAnimal(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        whatAnimal.setQuery();
        assertNotNull(whatAnimal.getSparqlQuery());
        assertFalse(whatAnimal.getSparqlQuery().isEmpty());
    }

    @Test
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"commonName\": {\"value\": \"Lion\"},"
                + "\"image\": {\"value\": \"https://example.com/lion.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        whatAnimal.setResults(mockResults);

        // Process the results
        whatAnimal.processResults();

        // Verify that at least one question contains expected text
        String expectedQuestionText = "animal";
        assertTrue(
                whatAnimal.getQs().stream().anyMatch(q -> q.getContent().contains(expectedQuestionText)),
                "Expected question text was not found in stored questions."
        );

        // Verify that at least one answer contains expected text
        String expectedAnswerText = "Lion";
        assertTrue(
                whatAnimal.getAs().stream().anyMatch(a -> a.getText().equals(expectedAnswerText)),
                "Expected answer text was not found in stored answers."
        );
    }

    @Test
    void needToSkipTest_Duplicates_ReturnTrue() {
        assertFalse(whatAnimal.needToSkip("Lion")); // First time: should be false
        assertTrue(whatAnimal.needToSkip("Lion")); // Second time: should be true
    }
}
