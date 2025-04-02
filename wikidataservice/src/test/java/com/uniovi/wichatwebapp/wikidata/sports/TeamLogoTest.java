package com.uniovi.wichatwebapp.wikidata.sports;

import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
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
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response for Team Logo
        JSONArray mockResults = new JSONArray("[{"
                + "\"name\": {\"value\": \"Real Madrid\"},"
                + "\"image\": {\"value\": \"https://example.com/realmadrid_logo.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        teamLogo.setResults(mockResults);

        // Process the results
        teamLogo.processResults();

        // Verify that at least one question contains expected text
        String expectedQuestionText = "escudo";
        assertTrue(
                teamLogo.getQs().stream().anyMatch(q -> q.getContent().contains(expectedQuestionText)),
                "Expected question text was not found in stored questions."
        );

        // Verify that at least one answer contains expected text
        String expectedAnswerText = "Real Madrid";
        assertTrue(
                teamLogo.getAs().stream().anyMatch(a -> a.getText().equals(expectedAnswerText)),
                "Expected answer text was not found in stored answers."
        );

        // Verify that the logo is correctly assigned
        String expectedImageUrl = "https://example.com/realmadrid_logo.jpg";
        assertTrue(
                teamLogo.getQs().stream().anyMatch(q -> q.getImageUrl().equals(expectedImageUrl)),
                "Expected image URL was not found in stored questions."
        );
    }

    @Test
    void processResultsTest_NoImage_AssignsDefaultImage() throws JSONException {
        // Mock JSON Response without a team logo
        JSONArray mockResults = new JSONArray("[{"
                + "\"name\": {\"value\": \"Real Madrid\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        teamLogo.setResults(mockResults);

        // Process the results
        teamLogo.processResults();

        // Verify that the default image is assigned
        String expectedDefaultImage = QuestionWikidata.DEFAULT_QUESTION_IMG;
        assertTrue(
                teamLogo.getQs().stream().anyMatch(q -> q.getImageUrl().equals(expectedDefaultImage)),
                "Default image was not assigned when no team logo was provided."
        );
    }


    @Test
    void needToSkipTest_DetectsDuplicateTeamLabels() {
        assertFalse(teamLogo.getTeamLabels().contains("Real Madrid"), "First check: should not contain Real Madrid.");
        teamLogo.getTeamLabels().add("Real Madrid"); // Manually add for test
        assertTrue(teamLogo.getTeamLabels().contains("Real Madrid"), "Second check: should now contain Real Madrid.");
        assertTrue(teamLogo.needToSkip("Real Madrid"), "Should skip duplicate team label.");
    }
    @Test
    void needToSkipTest_SkipsInvalidEntityNames() {
        assertTrue(WikidataUtils.isEntityName("Q12345"), "Q12345 should be recognized as an entity name.");
        assertTrue(teamLogo.needToSkip("Q12345"), "Should skip invalid team name.");
    }
    @Test
    void needToSkipTest_ProcessesValidEntriesCorrectly() {
        assertFalse(teamLogo.needToSkip("Manchester United"), "Valid team name should not be skipped.");
        assertFalse(teamLogo.needToSkip("FC Barcelona"), "Valid team name should not be skipped.");
    }



}
