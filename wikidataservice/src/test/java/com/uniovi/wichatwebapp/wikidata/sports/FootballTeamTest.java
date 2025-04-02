package com.uniovi.wichatwebapp.wikidata.sports;

import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
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
    void mockResultsTest_ProcessesMockedDataAndStoresQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response for Football Player
        JSONArray mockResults = new JSONArray("[{"
                + "\"name\": {\"value\": \"Lionel Messi\"},"
                + "\"team_name\": {\"value\": \"Inter Miami\"},"
                + "\"image\": {\"value\": \"https://example.com/messi.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        footballTeam.setResults(mockResults);

        // Process the results
        footballTeam.processResults();

        // Verify that at least one question contains expected text
        String expectedQuestionText = "Lionel Messi";
        assertTrue(
                footballTeam.getQs().stream().anyMatch(q -> q.getContent().contains(expectedQuestionText)),
                "Expected question text was not found in stored questions."
        );

        // Verify that at least one answer contains expected text
        String expectedAnswerText = "Inter Miami";
        assertTrue(
                footballTeam.getAs().stream().anyMatch(a -> a.getText().equals(expectedAnswerText)),
                "Expected answer text was not found in stored answers."
        );

        // Verify that an image was correctly assigned
        String expectedImageUrl = "https://example.com/messi.jpg";
        assertTrue(
                footballTeam.getQs().stream().anyMatch(q -> q.getImageUrl().equals(expectedImageUrl)),
                "Expected image URL was not found in stored questions."
        );
    }

    @Test
    void processResultsTest_NoImage_AssignsDefaultImage() throws JSONException {
        // Mock JSON Response without an image
        JSONArray mockResults = new JSONArray("[{"
                + "\"name\": {\"value\": \"Lionel Messi\"},"
                + "\"team_name\": {\"value\": \"Inter Miami\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        footballTeam.setResults(mockResults);

        // Process the results
        footballTeam.processResults();

        // Verify that the default image is assigned
        String expectedDefaultImage = QuestionWikidata.DEFAULT_QUESTION_IMG;
        assertTrue(
                footballTeam.getQs().stream().anyMatch(q -> q.getImageUrl().equals(expectedDefaultImage)),
                "Default image was not assigned when no player image was provided."
        );
    }

    @Test
    void needToSkipTest_DetectsDuplicateAthleteLabels() {
        assertFalse(footballTeam.getAthleteLabels().contains("Lionel Messi"), "First check: should not contain Messi.");
        footballTeam.getAthleteLabels().add("Lionel Messi"); // Manually add for test
        assertTrue(footballTeam.getAthleteLabels().contains("Lionel Messi"), "Second check: should now contain Messi.");
        assertTrue(footballTeam.needToSkip("Lionel Messi", "Inter Miami"), "Should skip duplicate athlete label.");
    }
    @Test
    void needToSkipTest_SkipsInvalidEntityNames() {
        assertTrue(WikidataUtils.isEntityName("Q12345"), "Q12345 should be recognized as an entity name.");
        assertTrue(footballTeam.needToSkip("Q12345", "Inter Miami"), "Should skip invalid athlete name.");
        assertTrue(footballTeam.needToSkip("Lionel Messi", "Q67890"), "Should skip invalid team name.");
    }
    @Test
    void needToSkipTest_ProcessesValidEntriesCorrectly() {
        assertFalse(footballTeam.needToSkip("Cristiano Ronaldo", "Al Nassr"), "Valid names should not be skipped.");
        assertFalse(footballTeam.needToSkip("Kylian Mbappé", "Paris Saint-Germain"), "Valid names should not be skipped.");
    }

}
