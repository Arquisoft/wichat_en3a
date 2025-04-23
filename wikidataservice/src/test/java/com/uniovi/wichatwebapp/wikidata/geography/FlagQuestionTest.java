package com.uniovi.wichatwebapp.wikidata.geography;

import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class FlagQuestionTest {
    private FlagQuestion flagQuestion;

    @BeforeEach
    void setUp() {
        flagQuestion = new FlagQuestion(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        flagQuestion.setQuery();
        assertNotNull(flagQuestion.getSparqlQuery());
        assertFalse(flagQuestion.getSparqlQuery().isEmpty());
    }

    @Test
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"countryLabel\": {\"value\": \"Spain\"},"
                + "\"flagLabel\": {\"value\": \"https://example.com/spain-flag.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        flagQuestion.setResults(mockResults);

        // Process the results
        flagQuestion.processResults();

        // Verify that at least one question contains expected text
        String expectedQuestionText = "bandera";
        assertTrue(
                flagQuestion.getQs().stream().anyMatch(q -> q.getContent().contains(expectedQuestionText)),
                "Expected question text was not found in stored questions."
        );

        // Verify that at least one answer contains expected text
        String expectedAnswerText = "Spain";
        assertTrue(
                flagQuestion.getAs().stream().anyMatch(a -> a.getText().equals(expectedAnswerText)),
                "Expected answer text was not found in stored answers."
        );
    }



    @Test
    void needToSkipTest_Duplicates_ReturnTrue() {
        assertFalse(flagQuestion.needToSkip("España", "https://example.com/spain-flag.jpg")); // First time: should be false
        assertTrue(flagQuestion.needToSkip("España", "https://example.com/spain-flag.jpg")); // Second time: should be true
    }
    @Test
    void needToSkipTest_DetectsDuplicateCountryLabels() {
        assertFalse(flagQuestion.getAnswerLabels().contains("Spain"), "First check: should not contain Spain.");
        flagQuestion.getAnswerLabels().add("Spain"); // Manually add for test
        assertTrue(flagQuestion.getAnswerLabels().contains("Spain"), "Second check: should now contain Spain.");
        assertTrue(flagQuestion.needToSkip("Spain", "https://example.com/spain-flag.jpg"), "Should skip duplicate country label.");
    }

    @Test
    void needToSkipTest_SkipsInvalidEntityNames() {
        assertTrue(WikidataUtils.isEntityName("Q12345"), "Q12345 should be recognized as an entity name.");
        assertTrue(flagQuestion.needToSkip("Q12345", "https://example.com/spain-flag.jpg"), "Should skip invalid country name.");
    }
    @Test
    void needToSkipTest_ProcessesValidEntriesCorrectly() {
        assertFalse(flagQuestion.needToSkip("France", "https://example.com/france-flag.jpg"), "Valid country name should not be skipped.");
        assertFalse(flagQuestion.needToSkip("Germany", "https://example.com/germany-flag.jpg"), "Valid country name should not be skipped.");
    }



}