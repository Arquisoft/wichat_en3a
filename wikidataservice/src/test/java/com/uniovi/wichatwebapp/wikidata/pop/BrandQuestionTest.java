package com.uniovi.wichatwebapp.wikidata.pop;

import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class BrandQuestionTest {
    private BrandQuestion brandQuestion;

    @BeforeEach
    void setUp() {
        brandQuestion = new BrandQuestion(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        brandQuestion.setQuery();
        assertNotNull(brandQuestion.getSparqlQuery());
        assertFalse(brandQuestion.getSparqlQuery().isEmpty());
    }

    @Test
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"brandLabel\": {\"value\": \"Nike\"},"
                + "\"logo\": {\"value\": \"https://example.com/nike-logo.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        brandQuestion.setResults(mockResults);

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
    void processResultsTest_NoImage_AssignsDefaultImage() throws JSONException {
        // Mock JSON Response without a brand logo
        JSONArray mockResults = new JSONArray("[{"
                + "\"brandLabel\": {\"value\": \"Nike\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        brandQuestion.setResults(mockResults);

        // Process the results
        brandQuestion.processResults();

        // Verify that the default image is assigned
        String expectedDefaultImage = QuestionWikidata.DEFAULT_QUESTION_IMG;
        assertTrue(
                brandQuestion.getQs().stream().anyMatch(q -> q.getImageUrl().equals(expectedDefaultImage)),
                "Default image was not assigned when no brand logo was provided."
        );
    }

    @Test
    void needToSkipTest_DetectsDuplicateBrandLabels() {
        assertFalse(brandQuestion.getBrandLabels().contains("Nike"), "First check: should not contain Nike.");
        brandQuestion.getBrandLabels().add("Nike"); // Manually add for test
        assertTrue(brandQuestion.getBrandLabels().contains("Nike"), "Second check: should now contain Nike.");
        assertTrue(brandQuestion.needToSkip("Nike"), "Should skip duplicate brand label.");
    }
    @Test
    void needToSkipTest_SkipsInvalidEntityNames() {
        assertTrue(WikidataUtils.isEntityName("Q12345"), "Q12345 should be recognized as an entity name.");
        assertTrue(brandQuestion.needToSkip("Q12345"), "Should skip invalid brand name.");
    }
    @Test
    void needToSkipTest_ProcessesValidEntriesCorrectly() {
        assertFalse(brandQuestion.needToSkip("Adidas"), "Valid brand name should not be skipped.");
        assertFalse(brandQuestion.needToSkip("Apple"), "Valid brand name should not be skipped.");
    }

}
