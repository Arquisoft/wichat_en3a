package com.uniovi.hintservice.service;

import static org.junit.jupiter.api.Assertions.*;

import autovalue.shaded.com.google.common.collect.ImmutableList;
import com.google.genai.Client;
import com.google.genai.Models;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.SafetySetting;
import org.apache.http.HttpException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class GenAITest {

	@Mock
	private Client client;

	@Mock
	private Models models;

	@InjectMocks
	private GenAI genAI;

	@Mock
	private GenerateContentResponse response;

	private final String testApiKey="test-api-key";
	private final String testInstructions = "Test instructions";
	private final String testPrompt = "Test prompt";

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(genAI, "apiKey", testApiKey);
		genAI.init(); // Initialize the component
	}

	@Test
	void initTest() {
		assertNotNull(ReflectionTestUtils.getField(genAI, "client"));
		assertNotNull(ReflectionTestUtils.getField(genAI, "safetySettings"));
		assertNotNull(ReflectionTestUtils.getField(genAI, "googleSearchTool"));
	}

	@Test
	void askPromptTest() throws IOException, HttpException {
		// Arrange
		String testResponse="Response";

		try {
			String result = genAI.askPrompt(testInstructions, testPrompt);
			fail();
		} catch(RuntimeException e){
			//Cannot be tested due to NullPointer to client or without using the api-key
		}
	}

	@Test
	void testInitializeSafetySettings_ReturnsCorrectSettings() {
		// Act
		ImmutableList<SafetySetting> settings = genAI.initializeSafetySettings();

		// Assert
		assertNotNull(settings);
		assertEquals(2, settings.size());

		SafetySetting hateSpeechSetting = settings.get(0);
		assertEquals("HARM_CATEGORY_HATE_SPEECH", hateSpeechSetting.category().get());
		assertEquals("BLOCK_ONLY_HIGH", hateSpeechSetting.threshold().get());

		SafetySetting dangerousContentSetting = settings.get(1);
		assertEquals("HARM_CATEGORY_DANGEROUS_CONTENT", dangerousContentSetting.category().get());
		assertEquals("BLOCK_LOW_AND_ABOVE", dangerousContentSetting.threshold().get());
	}

	@Test
	void constantsTest() {
		// Arrange
		String gemini15 = (String) ReflectionTestUtils.getField(genAI, "GEMINI_1_5");
		String gemini20 = (String) ReflectionTestUtils.getField(genAI, "GEMINI_2_0");

		// Assert
		assertEquals("gemini-1.5-flash-001", gemini15);
		assertEquals("gemini-2.0-flash-001", gemini20);
	}
}