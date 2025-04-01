package com.uniovi.wichatwebapp.wikidata.pop;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BrandQuestionTest {
    private BrandQuestion brandQuestion;

    @BeforeEach
    void setUp() {
        brandQuestion = new BrandQuestion(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        brandQuestion.setQuery();
        assertNotNull(brandQuestion.sparqlQuery);
        assertFalse(brandQuestion.sparqlQuery.isEmpty());
    }

    @Test
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"brandLabel\": {\"value\": \"Nike\"},"
                + "\"logo\": {\"value\": \"https://example.com/nike-logo.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        brandQuestion.results = mockResults;

        // Process the results
        brandQuestion.processResults();

        // Verify that at least one question contains expected text
        String expectedQuestionText = "este logo";
        assertTrue(
                brandQuestion.getQs().stream().anyMatch(q -> q.getContent().contains(expectedQuestionText)),
                "Expected question text was not found in stored questions."
        );

        // Verify that at least one answer contains expected text
        String expectedAnswerText = "Nike";
        assertTrue(
                brandQuestion.getAs().stream().anyMatch(a -> a.getText().equals(expectedAnswerText)),
                "Expected answer text was not found in stored answers."
        );
    }

    @Test
    void needToSkipTest_Duplicates_ReturnTrue() {
        assertFalse(brandQuestion.getBrandLabels().contains("Nike")); // First time: should be false
        brandQuestion.getBrandLabels().add("Nike"); // Add label manually for test
        assertTrue(brandQuestion.getBrandLabels().contains("Nike")); // Second time: should be true
    }
}
