package com.uniovi.wichatwebapp.wikidata.geography;

import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class MonumentNameQuestionTest {
    private MonumentNameQuestion monumentNameQuestion;

    @BeforeEach
    void setUp() {
        monumentNameQuestion = new MonumentNameQuestion(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        monumentNameQuestion.setQuery();
        assertNotNull(monumentNameQuestion.getSparqlQuery());
        assertFalse(monumentNameQuestion.getSparqlQuery().isEmpty());
    }

    @Test
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"monumentLabel\": {\"value\": \"Eiffel Tower\"},"
                + "\"image\": {\"value\": \"https://example.com/eiffel.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        monumentNameQuestion.setResults(mockResults);

        // Process the results
        monumentNameQuestion.processResults();

        // Verify that at least one question contains expected text
        String expectedQuestionText = "monumento";
        assertTrue(
                monumentNameQuestion.getQs().stream().anyMatch(q -> q.getContent().contains(expectedQuestionText)),
                "Expected question text was not found in stored questions."
        );

        // Verify that at least one answer contains expected text
        String expectedAnswerText = "Eiffel Tower";
        assertTrue(
                monumentNameQuestion.getAs().stream().anyMatch(a -> a.getText().equals(expectedAnswerText)),
                "Expected answer text was not found in stored answers."
        );
    }

    @Test
    void processResultsTest_NoImage_AssignsDefaultImage() throws JSONException {
        // Mock JSON Response without a monument image
        JSONArray mockResults = new JSONArray("[{"
                + "\"monumentLabel\": {\"value\": \"Eiffel Tower\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        monumentNameQuestion.setResults(mockResults);

        // Process the results
        monumentNameQuestion.processResults();

        // Verify that the default image is assigned
        String expectedDefaultImage = QuestionWikidata.DEFAULT_QUESTION_IMG;
        assertTrue(
                monumentNameQuestion.getQs().stream().anyMatch(q -> q.getImageUrl().equals(expectedDefaultImage)),
                "Default image was not assigned when no monument image was provided."
        );
    }

    @Test
    void needToSkipTest_DetectsDuplicateMonumentLabels() {
        assertFalse(monumentNameQuestion.getAnswerLabels().contains("Eiffel Tower"), "First check: should not contain Eiffel Tower.");
        monumentNameQuestion.getAnswerLabels().add("Eiffel Tower"); // Manually add for test
        assertTrue(monumentNameQuestion.getAnswerLabels().contains("Eiffel Tower"), "Second check: should now contain Eiffel Tower.");
        assertTrue(monumentNameQuestion.needToSkip("Eiffel Tower"), "Should skip duplicate monument label.");
    }
    @Test
    void needToSkipTest_SkipsInvalidEntityNames() {
        assertTrue(WikidataUtils.isEntityName("Q12345"), "Q12345 should be recognized as an entity name.");
        assertTrue(monumentNameQuestion.needToSkip("Q12345"), "Should skip invalid monument name.");
    }
    @Test
    void needToSkipTest_ProcessesValidEntriesCorrectly() {
        assertFalse(monumentNameQuestion.needToSkip("Statue of Liberty"), "Valid monument name should not be skipped.");
        assertFalse(monumentNameQuestion.needToSkip("Colosseum"), "Valid monument name should not be skipped.");
    }

}