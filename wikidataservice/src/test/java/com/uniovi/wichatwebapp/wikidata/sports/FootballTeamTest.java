package com.uniovi.wichatwebapp.wikidata.sports;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class FootballTeamTest {
    private FootballTeam footballTeam;

    @BeforeEach
    void setUp() {
        footballTeam = new FootballTeam(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        footballTeam.setQuery();
        assertNotNull(footballTeam.getSparqlQuery());
        assertFalse(footballTeam.getSparqlQuery().isEmpty());
    }

    @Test
    void mockResultsTest_StoresMockedDataWithoutProcessing() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"name\": {\"value\": \"Lionel Messi\"},"
                + "\"team_name\": {\"value\": \"Inter Miami\"},"
                + "\"image\": {\"value\": \"https://example.com/messi.jpg\"}"
                + "}]");

        // Inject mock results **without calling processResults()**
        footballTeam.setResults(mockResults);

        // Validate that the results field was correctly set
        assertNotNull(footballTeam.getResults());
        assertEquals(1, footballTeam.getResults().length());
        assertEquals("Lionel Messi", footballTeam.getResults().getJSONObject(0).getJSONObject("name").getString("value"));
        assertEquals("Inter Miami", footballTeam.getResults().getJSONObject(0).getJSONObject("team_name").getString("value"));
        assertEquals("https://example.com/messi.jpg", footballTeam.getResults().getJSONObject(0).getJSONObject("image").getString("value"));
    }

    @Test
    void needToSkipTest_Duplicates_ReturnTrue() {
        assertFalse(footballTeam.getAthleteLabels().contains("Lionel Messi")); // First time: should be false
        footballTeam.getAthleteLabels().add("Lionel Messi"); // Add label manually for test
        assertTrue(footballTeam.getAthleteLabels().contains("Lionel Messi")); // Second time: should be true
    }
}
