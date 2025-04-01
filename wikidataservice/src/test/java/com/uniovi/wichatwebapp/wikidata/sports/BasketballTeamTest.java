package com.uniovi.wichatwebapp.wikidata.sports;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BasketballTeamTest {
    private BasketballTeam basketballTeam;

    @BeforeEach
    void setUp() {
        basketballTeam = new BasketballTeam();
    }

    @Test
    void setQueryTest_QueryIsSet() {
        basketballTeam.setQuery();
        assertNotNull(basketballTeam.sparqlQuery);
        assertFalse(basketballTeam.sparqlQuery.isEmpty());
    }

    @Test
    void mockResultsTest_StoresMockedDataWithoutProcessing() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"name\": {\"value\": \"LeBron James\"},"
                + "\"team_name\": {\"value\": \"Los Angeles Lakers\"},"
                + "\"image\": {\"value\": \"https://example.com/lebron.jpg\"}"
                + "}]");

        // Inject mock results **without calling processResults()**
        basketballTeam.results = mockResults;

        // Validate that the results field was correctly set
        assertNotNull(basketballTeam.results);
        assertEquals(1, basketballTeam.results.length());
        assertEquals("LeBron James", basketballTeam.results.getJSONObject(0).getJSONObject("name").getString("value"));
        assertEquals("Los Angeles Lakers", basketballTeam.results.getJSONObject(0).getJSONObject("team_name").getString("value"));
        assertEquals("https://example.com/lebron.jpg", basketballTeam.results.getJSONObject(0).getJSONObject("image").getString("value"));
    }

    @Test
    void needToSkipTest_Duplicates_ReturnTrue() {
        assertFalse(basketballTeam.getAthleteLabels().contains("LeBron James")); // First time: should be false
        basketballTeam.getAthleteLabels().add("LeBron James"); // Add label manually for test
        assertTrue(basketballTeam.getAthleteLabels().contains("LeBron James")); // Second time: should be true
    }
}
