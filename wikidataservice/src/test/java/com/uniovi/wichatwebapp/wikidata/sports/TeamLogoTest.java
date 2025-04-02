package com.uniovi.wichatwebapp.wikidata.sports;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class TeamLogoTest {
    private TeamLogo teamLogo;

    @BeforeEach
    void setUp() {
        teamLogo = new TeamLogo(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        teamLogo.setQuery();
        assertNotNull(teamLogo.getSparqlQuery());
        assertFalse(teamLogo.getSparqlQuery().isEmpty());
    }

    @Test
    void mockResultsTest_StoresMockedDataWithoutProcessing() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"name\": {\"value\": \"Manchester United\"},"
                + "\"image\": {\"value\": \"https://example.com/manutd-logo.jpg\"}"
                + "}]");

        // Inject mock results **without calling processResults()**
        teamLogo.setResults(mockResults);

        // Validate that the results field was correctly set
        assertNotNull(teamLogo.getResults());
        assertEquals(1, teamLogo.getResults().length());
        assertEquals("Manchester United", teamLogo.getResults().getJSONObject(0).getJSONObject("name").getString("value"));
        assertEquals("https://example.com/manutd-logo.jpg", teamLogo.getResults().getJSONObject(0).getJSONObject("image").getString("value"));
    }

}
