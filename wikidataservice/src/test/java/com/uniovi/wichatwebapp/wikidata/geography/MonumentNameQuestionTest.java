package com.uniovi.wichatwebapp.wikidata.geography;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MonumentNameQuestionTest {
    private MonumentNameQuestion monumentNameQuestion;

    @BeforeEach
    void setUp() {
        monumentNameQuestion = new MonumentNameQuestion(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        monumentNameQuestion.setQuery();
        assertNotNull(monumentNameQuestion.sparqlQuery);
        assertFalse(monumentNameQuestion.sparqlQuery.isEmpty());
    }

    @Test
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"monumentLabel\": {\"value\": \"Eiffel Tower\"},"
                + "\"image\": {\"value\": \"https://example.com/eiffel.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        monumentNameQuestion.results = mockResults;

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
    void needToSkipTest_Duplicates_ReturnTrue() {
        assertFalse(monumentNameQuestion.needToSkip("Eiffel Tower")); // First time: should be false
        assertTrue(monumentNameQuestion.needToSkip("Eiffel Tower")); // Second time: should be true
    }

}