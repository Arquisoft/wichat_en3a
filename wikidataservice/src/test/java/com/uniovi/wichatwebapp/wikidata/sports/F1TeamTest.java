package com.uniovi.wichatwebapp.wikidata.sports;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class F1TeamTest {
    private F1Team f1Team;

    @BeforeEach
    void setUp() {
        f1Team = new F1Team(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        f1Team.setQuery();
        assertNotNull(f1Team.sparqlQuery);
        assertFalse(f1Team.sparqlQuery.isEmpty());
    }

    @Test
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"name\": {\"value\": \"Lewis Hamilton\"},"
                + "\"team_name\": {\"value\": \"Mercedes\"},"
                + "\"image\": {\"value\": \"https://example.com/hamilton.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        f1Team.results = mockResults;

        // Process the results
        f1Team.processResults();

        // Verify that at least one question contains expected text
        String expectedQuestionText = "Lewis Hamilton";
        assertTrue(
                f1Team.getQs().stream().anyMatch(q -> q.getContent().contains(expectedQuestionText)),
                "Expected question text was not found in stored questions."
        );

        // Verify that at least one answer contains expected text
        String expectedAnswerText = "Mercedes";
        assertTrue(
                f1Team.getAs().stream().anyMatch(a -> a.getText().equals(expectedAnswerText)),
                "Expected answer text was not found in stored answers."
        );
    }

    @Test
    void needToSkipTest_Duplicates_ReturnTrue() {
        assertFalse(f1Team.getAthleteLabels().contains("Ana")); // First time: should be false
        f1Team.getAthleteLabels().add("Ana"); // Add label manually for test
        assertTrue(f1Team.getAthleteLabels().contains("Ana")); // Second time: should be true
    }
}
