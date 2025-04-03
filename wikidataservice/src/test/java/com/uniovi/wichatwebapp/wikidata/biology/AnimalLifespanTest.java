package com.uniovi.wichatwebapp.wikidata.biology;


import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


class AnimalLifespanTest {
    private AnimalLifespan animalLifespan;

    @BeforeEach
    void setUp() {
        animalLifespan = new AnimalLifespan(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        animalLifespan.setQuery();
        assertNotNull(animalLifespan.getSparqlQuery());
        assertFalse(animalLifespan.getSparqlQuery().isEmpty());
    }

    @Test
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"commonName\": {\"value\": \"Lion\"},"
                + "\"lifespan\": {\"value\": \"15 years\"},"
                + "\"image\": {\"value\": \"https://example.com/lion.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        animalLifespan.setResults(mockResults);
        // Process the results
        animalLifespan.processResults();


        // Verify that at least one question contains expected text
        String expectedQuestionText = "Lion";
        assertTrue(
                animalLifespan.getQs().stream().anyMatch(q -> q.getContent().contains(expectedQuestionText)),
                "Expected question text was not found in stored questions."
        );

        // Verify that at least one answer contains expected text
        String expectedAnswerText = "15 years";
        assertTrue(
                animalLifespan.getAs().stream().anyMatch(a -> a.getText().equals(expectedAnswerText)),
                "Expected answer text was not found in stored answers."
        );
    }

    @Test
    void processResultsTest_NoImage_AssignsDefaultImage() throws JSONException {
        // Mock JSON Response without an image
        JSONArray mockResults = new JSONArray("[{"
                + "\"commonName\": {\"value\": \"Lion\"},"
                + "\"lifespan\": {\"value\": \"15 years\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        animalLifespan.setResults(mockResults);

        // Process the results
        animalLifespan.processResults();

        // Verify that the default image is assigned
        String expectedDefaultImage = QuestionWikidata.DEFAULT_QUESTION_IMG;
        assertTrue(
                animalLifespan.getQs().stream().anyMatch(q -> q.getImageUrl().equals(expectedDefaultImage)),
                "Default image was not assigned when no image was provided."
        );
    }

    @Test
    void needToSkipTest_DetectsDuplicateAnimalLabels() {
        assertFalse(animalLifespan.getAnimalLabels().contains("Lion"), "First check: should not contain Lion.");
        animalLifespan.getAnimalLabels().add("Lion"); // Manually add for test
        assertTrue(animalLifespan.getAnimalLabels().contains("Lion"), "Second check: should now contain Lion.");
        assertTrue(animalLifespan.needToSkip("Lion", "15 years"), "Should skip duplicate animal label.");
    }
    @Test
    void needToSkipTest_SkipsInvalidEntityNames() {
        assertTrue(WikidataUtils.isEntityName("Q12345"), "Q12345 should be recognized as an entity name.");
        assertTrue(animalLifespan.needToSkip("Q12345", "15 years"), "Should skip invalid animal name.");
    }
    @Test
    void needToSkipTest_ProcessesValidEntriesCorrectly() {
        assertFalse(animalLifespan.needToSkip("Elephant", "70 years"), "Valid animal name should not be skipped.");
        assertFalse(animalLifespan.needToSkip("Blue Whale", "80 years"), "Valid animal name should not be skipped.");
    }


}