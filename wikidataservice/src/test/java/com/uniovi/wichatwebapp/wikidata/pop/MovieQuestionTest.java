package com.uniovi.wichatwebapp.wikidata.pop;

import com.uniovi.wichatwebapp.wikidata.QuestionWikidata;
import com.uniovi.wichatwebapp.wikidata.WikidataUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class MovieQuestionTest {
    private MovieQuestion movieQuestion;

    @BeforeEach
    void setUp() {
        movieQuestion = new MovieQuestion(); // Instantiate with English language
    }

    @Test
    void setQueryTest_QueryIsSet() {
        movieQuestion.setQuery();
        assertNotNull(movieQuestion.getSparqlQuery());
        assertFalse(movieQuestion.getSparqlQuery().isEmpty());
    }

    @Test
    void processResultsTest_ValidData_StoresMockedQuestionsAndAnswers() throws JSONException {
        // Mock JSON Response
        JSONArray mockResults = new JSONArray("[{"
                + "\"movieLabel\": {\"value\": \"Inception\"},"
                + "\"releaseYear\": {\"value\": \"2010\"},"
                + "\"poster\": {\"value\": \"https://example.com/inception.jpg\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        movieQuestion.setResults(mockResults);

        // Process the results
        movieQuestion.processResults();

        // Verify that at least one question contains expected text
        String expectedQuestionText = "Inception";
        assertTrue(
                movieQuestion.getQs().stream().anyMatch(q -> q.getContent().contains(expectedQuestionText)),
                "Expected question text was not found in stored questions."
        );

        // Verify that at least one answer contains expected text
        String expectedAnswerText = "2010";
        assertTrue(
                movieQuestion.getAs().stream().anyMatch(a -> a.getText().equals(expectedAnswerText)),
                "Expected answer text was not found in stored answers."
        );
    }
    @Test
    void processResultsTest_NoImage_AssignsDefaultImage() throws JSONException {
        // Mock JSON Response without a movie poster
        JSONArray mockResults = new JSONArray("[{"
                + "\"movieLabel\": {\"value\": \"Inception\"},"
                + "\"releaseYear\": {\"value\": \"2010\"}"
                + "}]");

        // Inject mock results before calling `processResults()`
        movieQuestion.setResults(mockResults);

        // Process the results
        movieQuestion.processResults();

        // Verify that the default image is assigned
        String expectedDefaultImage = QuestionWikidata.DEFAULT_QUESTION_IMG;
        assertTrue(
                movieQuestion.getQs().stream().anyMatch(q -> q.getImageUrl().equals(expectedDefaultImage)),
                "Default image was not assigned when no movie poster was provided."
        );
    }


    @Test
    void needToSkipTest_DetectsDuplicateMovieLabels() {
        assertFalse(movieQuestion.getMovieLabels().contains("Inception"), "First check: should not contain Inception.");
        movieQuestion.getMovieLabels().add("Inception"); // Manually add for test
        assertTrue(movieQuestion.getMovieLabels().contains("Inception"), "Second check: should now contain Inception.");
        assertTrue(movieQuestion.needToSkip("Inception"), "Should skip duplicate movie label.");
    }
    @Test
    void needToSkipTest_SkipsInvalidEntityNames() {
        assertTrue(WikidataUtils.isEntityName("Q12345"), "Q12345 should be recognized as an entity name.");
        assertTrue(movieQuestion.needToSkip("Q12345"), "Should skip invalid movie name.");
    }
    @Test
    void needToSkipTest_ProcessesValidEntriesCorrectly() {
        assertFalse(movieQuestion.needToSkip("Titanic"), "Valid movie name should not be skipped.");
        assertFalse(movieQuestion.needToSkip("The Dark Knight"), "Valid movie name should not be skipped.");
    }

}
