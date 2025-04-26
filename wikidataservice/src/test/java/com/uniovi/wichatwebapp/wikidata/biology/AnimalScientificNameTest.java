package com.uniovi.wichatwebapp.wikidata.biology;

import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
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
    void processResultsTest_NoImage_AssignsDefaultImage() throws JSONException {
        // Mock JSON Response without an image
        JSONArray mockResults = new JSONArray("[{"
                + "\"commonName\": {\"value\": \"Lion\"},"
                + "\"scientificName\": {\"value\": \"Panthera leo\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        animalScientificName.setResults(mockResults);

        // Process the results
        animalScientificName.processResults();

        // Verify that the default image is assigned
        String expectedDefaultImage = QuestionWikidata.DEFAULT_QUESTION_IMG;
        assertTrue(
                animalScientificName.getQs().stream().anyMatch(q -> q.getImageUrl().equals(expectedDefaultImage)),
                "Default image was not assigned when no image was provided."
        );
    }


    @Test
    void needToSkipTest_DetectsDuplicateAnimalLabels() {
        assertFalse(animalScientificName.getAnimalLabels().contains("Lion"), "First check: should not contain Lion.");
        animalScientificName.getAnimalLabels().add("Lion"); // Manually add for test
        assertTrue(animalScientificName.getAnimalLabels().contains("Lion"), "Second check: should now contain Lion.");
        assertTrue(animalScientificName.needToSkip("Lion", "Panthera leo"), "Should skip duplicate animal label.");
    }
    @Test
    void needToSkipTest_SkipsInvalidEntityNames() {
        assertTrue(WikidataUtils.isEntityName("Q12345"), "Q12345 should be recognized as an entity name.");
        assertTrue(animalScientificName.needToSkip("Q12345", "Panthera leo"), "Should skip invalid animal name.");
    }
    @Test
    void needToSkipTest_ProcessesValidEntriesCorrectly() {
        assertFalse(animalScientificName.needToSkip("Elephant", "Loxodonta africana"), "Valid animal name should not be skipped.");
        assertFalse(animalScientificName.needToSkip("Blue Whale", "Balaenoptera musculus"), "Valid animal name should not be skipped.");
    }

}
