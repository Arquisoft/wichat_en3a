package com.uniovi.wichatwebapp.wikidata.sports;

import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class F1TeamTest {
    private F1Team f1Team;

    @BeforeEach
    void setUp() {
        f1Team = new F1Team();
    }

    @Test
    void setQueryTest_QueryIsSet() {
        f1Team.setQuery();
        assertNotNull(f1Team.getSparqlQuery());
        assertFalse(f1Team.getSparqlQuery().isEmpty());
    }

    @Test
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response for Football Team
        JSONArray mockResults = new JSONArray("[{"
                + "\"name\": {\"value\": \"Lewis Hamilton\"},"
                + "\"team_name\": {\"value\": \"Mercedes\"},"
                + "\"image\": {\"value\": \"https://example.com/messi.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        FootballTeam footballTeam = new FootballTeam();
        footballTeam.setResults(mockResults);

        // Process the results
        footballTeam.processResults();

        // Verify that at least one question contains expected text
        String expectedQuestionText = "Lewis Hamilton";
        assertTrue(
                footballTeam.getQs().stream().anyMatch(q -> q.getContent().contains(expectedQuestionText)),
                "Expected question text was not found in stored questions."
        );

        // Verify that at least one answer contains expected text
        String expectedAnswerText = "Mercedes";
        assertTrue(
                footballTeam.getAs().stream().anyMatch(a -> a.getText().equals(expectedAnswerText)),
                "Expected answer text was not found in stored answers."
        );
    }
    @Test
    void processResultsTest_NoImage_AssignsDefaultImage() throws JSONException {
        // Mock JSON Response without a driver image
        JSONArray mockResults = new JSONArray("[{"
                + "\"name\": {\"value\": \"Lewis Hamilton\"},"
                + "\"team_name\": {\"value\": \"Mercedes\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        f1Team.setResults(mockResults);

        // Process the results
        f1Team.processResults();

        // Verify that the default image is assigned
        String expectedDefaultImage = QuestionWikidata.DEFAULT_QUESTION_IMG;
        assertTrue(
                f1Team.getQs().stream().anyMatch(q -> q.getImageUrl().equals(expectedDefaultImage)),
                "Default image was not assigned when no driver image was provided."
        );
    }

    @Test
    void needToSkipTest_DetectsDuplicateAthleteLabels() {
        assertFalse(f1Team.getAnswerLabels().contains("Lewis Hamilton"), "First check: should not contain Hamilton.");
        f1Team.getAnswerLabels().add("Lewis Hamilton"); // Manually add for test
        assertTrue(f1Team.getAnswerLabels().contains("Lewis Hamilton"), "Second check: should now contain Hamilton.");
        assertTrue(f1Team.needToSkip("Lewis Hamilton", "Mercedes"), "Should skip duplicate athlete label.");
    }

    @Test
    void needToSkipTest_SkipsInvalidEntityNames() {
        assertTrue(WikidataUtils.isEntityName("Q12345"), "Q12345 should be recognized as an entity name.");
        assertTrue(f1Team.needToSkip("Q12345", "Mercedes"), "Should skip invalid athlete name.");
    }

    @Test
    void needToSkipTest_ProcessesValidEntriesCorrectly() {
        assertFalse(f1Team.needToSkip("Max Verstappen", "Red Bull"), "Valid names should not be skipped.");
        assertFalse(f1Team.needToSkip("Charles Leclerc", "Ferrari"), "Valid names should not be skipped.");
    }
}
