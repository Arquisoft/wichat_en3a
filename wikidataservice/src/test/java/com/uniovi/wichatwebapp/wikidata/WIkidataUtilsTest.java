package com.uniovi.wichatwebapp.wikidata;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.AnswerCategory;
import com.uniovi.wichatwebapp.repositories.AnswerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // No Spring context!
class WikidataUtilsTest {

    @Test
    void isEntityNameTest_ValidEntity_ReturnsTrue() {
        assertTrue(WikidataUtils.isEntityName("Q123456"));
    }

    @Test
    void isEntityNameTest_InvalidEntity_ReturnsFalse() {
        assertFalse(WikidataUtils.isEntityName("QABC123")); // Contains non-digit characters
        assertFalse(WikidataUtils.isEntityName("123Q456")); // Starts incorrectly
        assertFalse(WikidataUtils.isEntityName("randomText")); // Not in expected format
    }
    @Test
    void notAllowedExtensionTest_AllowedExtension_ReturnsFalse() {
        assertFalse(WikidataUtils.notAllowedExtension("image.png"));
        assertFalse(WikidataUtils.notAllowedExtension("photo.jpg"));
        assertFalse(WikidataUtils.notAllowedExtension("logo.svg"));
    }

    @Test
    void notAllowedExtensionTest_NotAllowedExtension_ReturnsTrue() {
        assertTrue(WikidataUtils.notAllowedExtension("document.pdf"));
        assertTrue(WikidataUtils.notAllowedExtension("script.js"));
        assertTrue(WikidataUtils.notAllowedExtension("style.css"));
    }
    @Test
    void capitalizeTest_ValidString_ReturnsCapitalized() {
        assertEquals("Hello", WikidataUtils.capitalize("hello"));
        assertEquals("World", WikidataUtils.capitalize("world"));
    }

    @Test
    void capitalizeTest_AlreadyCapitalized_ReturnsSameString() {
        assertEquals("Java", WikidataUtils.capitalize("Java"));
    }

    @Test
    void capitalizeTest_EmptyOrNullString_ReturnsSameString() {
        assertEquals("", WikidataUtils.capitalize(""));
        assertNull(WikidataUtils.capitalize(null));
    }

}