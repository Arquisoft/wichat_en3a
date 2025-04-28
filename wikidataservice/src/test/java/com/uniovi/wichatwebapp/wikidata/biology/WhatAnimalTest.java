package com.uniovi.wichatwebapp.wikidata.biology;

import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
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
    void processResultsTest_NoImage_AssignsDefaultImage() throws JSONException {
        // Mock JSON Response without an image
        JSONArray mockResults = new JSONArray("[{"
                + "\"commonName\": {\"value\": \"Lion\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        whatAnimal.setResults(mockResults);

        // Process the results
        whatAnimal.processResults();

        // Verify that the default image is assigned
        String expectedDefaultImage = QuestionWikidata.DEFAULT_QUESTION_IMG;
        assertTrue(
                whatAnimal.getQs().stream().anyMatch(q -> q.getImageUrl().equals(expectedDefaultImage)),
                "Default image was not assigned when no image was provided."
        );
    }

    @Test
    void needToSkipTest_DetectsDuplicateAnimalLabels() {
        assertFalse(whatAnimal.getAnswerLabels().contains("Lion"), "First check: should not contain Lion.");
        whatAnimal.getAnswerLabels().add("Lion"); // Manually add for test
        assertTrue(whatAnimal.getAnswerLabels().contains("Lion"), "Second check: should now contain Lion.");
        assertTrue(whatAnimal.needToSkip("Lion"), "Should skip duplicate animal label.");
    }
    @Test
    void needToSkipTest_SkipsInvalidEntityNames() {
        assertTrue(WikidataUtils.isEntityName("Q12345"), "Q12345 should be recognized as an entity name.");
        assertTrue(whatAnimal.needToSkip("Q12345"), "Should skip invalid animal name.");
    }
    @Test
    void needToSkipTest_ProcessesValidEntriesCorrectly() {
        assertFalse(whatAnimal.needToSkip("Elephant"), "Valid animal name should not be skipped.");
        assertFalse(whatAnimal.needToSkip("Blue Whale"), "Valid animal name should not be skipped.");
    }

}
