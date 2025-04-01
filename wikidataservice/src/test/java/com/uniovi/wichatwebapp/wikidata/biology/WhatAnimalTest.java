package com.uniovi.wichatwebapp.wikidata.biology;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WhatAnimalTest {
    private WhatAnimal whatAnimal;

    @BeforeEach
    void setUp() {
        whatAnimal = new WhatAnimal(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        whatAnimal.setQuery();
        assertNotNull(whatAnimal.sparqlQuery);
        assertFalse(whatAnimal.sparqlQuery.isEmpty());
    }

    @Test
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"commonName\": {\"value\": \"Lion\"},"
                + "\"image\": {\"value\": \"https://example.com/lion.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        whatAnimal.results = mockResults;

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
