package com.uniovi.wichatwebapp.wikidata.sports;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TeamLogoTest {
    private TeamLogo teamLogo;

    @BeforeEach
    void setUp() {
        teamLogo = new TeamLogo(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        teamLogo.setQuery();
        assertNotNull(teamLogo.sparqlQuery);
        assertFalse(teamLogo.sparqlQuery.isEmpty());
    }

    @Test
    void mockResultsTest_StoresMockedDataWithoutProcessing() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"name\": {\"value\": \"Manchester United\"},"
                + "\"image\": {\"value\": \"https://example.com/manutd-logo.jpg\"}"
                + "}]");

        // Inject mock results **without calling processResults()**
        teamLogo.results = mockResults;

        // Validate that the results field was correctly set
        assertNotNull(teamLogo.results);
        assertEquals(1, teamLogo.results.length());
        assertEquals("Manchester United", teamLogo.results.getJSONObject(0).getJSONObject("name").getString("value"));
        assertEquals("https://example.com/manutd-logo.jpg", teamLogo.results.getJSONObject(0).getJSONObject("image").getString("value"));
    }

}
