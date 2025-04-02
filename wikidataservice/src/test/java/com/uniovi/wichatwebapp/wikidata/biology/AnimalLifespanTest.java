package com.uniovi.wichatwebapp.wikidata.biology;


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
    void needToSkipTest_Duplicates_ReturnTrue() {
        assertFalse(animalLifespan.needToSkip("Lion", "15 years")); // First time: should be false
        assertTrue(animalLifespan.needToSkip("Lion", "15 years")); // Second time: should be true
    }


}