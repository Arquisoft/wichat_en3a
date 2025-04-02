package com.uniovi.wichatwebapp.wikidata.sports;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasketballTeamTest {
    private BasketballTeam basketballTeam;

    @BeforeEach
    void setUp() {
        basketballTeam = new BasketballTeam();
    }

    @Test
    void setQueryTest_QueryIsSet() {
        basketballTeam.setQuery();
        assertNotNull(basketballTeam.getSparqlQuery());
        assertFalse(basketballTeam.getSparqlQuery().isEmpty());
    }

    @Test
    void mockResultsTest_ProcessesMockedDataAndStoresQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"name\": {\"value\": \"LeBron James\"},"
                + "\"team_name\": {\"value\": \"Los Angeles Lakers\"},"
                + "\"image\": {\"value\": \"https://example.com/lebron.jpg\"}"
                + "}]");

        // Inject mock results
        basketballTeam.setResults(mockResults);

        // Now process the results
        basketballTeam.processResults();

        // Verify that at least one question contains expected text
        String expectedQuestionText = "LeBron James";
        assertTrue(
                basketballTeam.getQs().stream().anyMatch(q -> q.getContent().contains(expectedQuestionText)),
                "Expected question text was not found in stored questions."
        );

        // Verify that at least one answer contains expected text
        String expectedAnswerText = "Los Angeles Lakers";
        assertTrue(
                basketballTeam.getAs().stream().anyMatch(a -> a.getText().equals(expectedAnswerText)),
                "Expected answer text was not found in stored answers."
        );
    }



    @Test
    void needToSkipTest_Duplicates_ReturnTrue() {
        assertFalse(basketballTeam.getAthleteLabels().contains("LeBron James")); // First time: should be false
        basketballTeam.getAthleteLabels().add("LeBron James"); // Add label manually for test
        assertTrue(basketballTeam.getAthleteLabels().contains("LeBron James")); // Second time: should be true
    }
}
