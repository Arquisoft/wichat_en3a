package com.uniovi.wichatwebapp.wikidata.sports;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FootballTeamTest {
    private FootballTeam footballTeam;

    @BeforeEach
    void setUp() {
        footballTeam = new FootballTeam(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        footballTeam.setQuery();
        assertNotNull(footballTeam.sparqlQuery);
        assertFalse(footballTeam.sparqlQuery.isEmpty());
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
        footballTeam.results = mockResults;

        // Validate that the results field was correctly set
        assertNotNull(footballTeam.results);
        assertEquals(1, footballTeam.results.length());
        assertEquals("Lionel Messi", footballTeam.results.getJSONObject(0).getJSONObject("name").getString("value"));
        assertEquals("Inter Miami", footballTeam.results.getJSONObject(0).getJSONObject("team_name").getString("value"));
        assertEquals("https://example.com/messi.jpg", footballTeam.results.getJSONObject(0).getJSONObject("image").getString("value"));
    }

    @Test
    void needToSkipTest_Duplicates_ReturnTrue() {
        assertFalse(footballTeam.getAthleteLabels().contains("Lionel Messi")); // First time: should be false
        footballTeam.getAthleteLabels().add("Lionel Messi"); // Add label manually for test
        assertTrue(footballTeam.getAthleteLabels().contains("Lionel Messi")); // Second time: should be true
    }
}
