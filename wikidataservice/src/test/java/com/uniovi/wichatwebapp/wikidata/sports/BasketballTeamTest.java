package com.uniovi.wichatwebapp.wikidata.sports;

import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
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
    void processResultsTest_NoImage_AssignsDefaultImage() throws JSONException {
        // Mock JSON Response without an image
        JSONArray mockResults = new JSONArray("[{"
                + "\"name\": {\"value\": \"LeBron James\"},"
                + "\"team_name\": {\"value\": \"Los Angeles Lakers\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        basketballTeam.setResults(mockResults);

        // Process the results
        basketballTeam.processResults();

        // Verify that the default image is assigned
        String expectedDefaultImage = QuestionWikidata.DEFAULT_QUESTION_IMG;
        assertTrue(
                basketballTeam.getQs().stream().anyMatch(q -> q.getImageUrl().equals(expectedDefaultImage)),
                "Default image was not assigned when no player image was provided."
        );
    }

    @Test
    void needToSkipTest_DetectsDuplicateAthleteLabels() {
        assertFalse(basketballTeam.getAnswerLabels().contains("LeBron James"), "First check: should not contain LeBron.");
        basketballTeam.getAnswerLabels().add("LeBron James"); // Manually add for test
        assertTrue(basketballTeam.getAnswerLabels().contains("LeBron James"), "Second check: should now contain LeBron.");
        assertTrue(basketballTeam.needToSkip("LeBron James", "Los Angeles Lakers"), "Should skip duplicate athlete label.");
    }

    @Test
    void needToSkipTest_SkipsInvalidEntityNames() {
        assertTrue(WikidataUtils.isEntityName("Q12345"), "Q12345 should be recognized as an entity name.");
        assertTrue(basketballTeam.needToSkip("Q12345", "Los Angeles Lakers"), "Should skip invalid athlete name.");
    }

    @Test
    void needToSkipTest_ProcessesValidEntriesCorrectly() {
        assertFalse(basketballTeam.needToSkip("Stephen Curry", "Golden State Warriors"), "Valid names should not be skipped.");
        assertFalse(basketballTeam.needToSkip("Kevin Durant", "Phoenix Suns"), "Valid names should not be skipped.");
    }
}
