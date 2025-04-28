package com.uniovi.wichatwebapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InsertSampleDataMultithreadedServiceTest {
    @Mock
    private QuestionService questionService;

    @InjectMocks
    private InsertSampleDataMultithreadedService service;

    @Test
    void insertFlagQuestionsTest() {
        // Act
        CompletableFuture<Void> future = service.insertFlagQuestions();

        // Wait for completion
        future.join();

        // Assert
        verify(questionService).saveAllQuestions(any());
        verify(questionService).saveAllAnswers(any());
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    void insertMovieQuestionsTest() throws Exception {
        CompletableFuture<Void> future = service.insertMovieQuestions();
        future.join();
        verify(questionService).saveAllQuestions(any());
        verify(questionService).saveAllAnswers(any());
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    void insertBrandQuestionsTest() throws Exception {
        CompletableFuture<Void> future = service.insertBrandQuestions();
        future.join();
        verify(questionService).saveAllQuestions(any());
        verify(questionService).saveAllAnswers(any());
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    void insertMonumentCountryQuestionsTest() throws Exception {
        CompletableFuture<Void> future = service.insertMonumentCountryQuestions();
        future.join();
        verify(questionService).saveAllQuestions(any());
        verify(questionService).saveAllAnswers(any());
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    void insertMonumentNameQuestionsTest() throws Exception {
        CompletableFuture<Void> future = service.insertMonumentNameQuestions();
        future.join();
        verify(questionService).saveAllQuestions(any());
        verify(questionService).saveAllAnswers(any());
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    void insertBasketballTeamQuestionsTest() throws Exception {
        CompletableFuture<Void> future = service.insertBasketballTeamQuestions();
        future.join();
        verify(questionService).saveAllQuestions(any());
        verify(questionService).saveAllAnswers(any());
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    void insertF1TeamQuestionsTest() throws Exception {
        CompletableFuture<Void> future = service.insertF1TeamQuestions();
        future.join();
        verify(questionService).saveAllQuestions(any());
        verify(questionService).saveAllAnswers(any());
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    void insertFootballTeamQuestionsTest() throws Exception {
        CompletableFuture<Void> future = service.insertFootballTeamQuestions();
        future.join();
        verify(questionService).saveAllQuestions(any());
        verify(questionService).saveAllAnswers(any());
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    void insertTeamLogoQuestionsTest() throws Exception {
        CompletableFuture<Void> future = service.insertTeamLogoQuestions();
        future.join();
        verify(questionService).saveAllQuestions(any());
        verify(questionService).saveAllAnswers(any());
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    void insertWhatAnimalQuestionsTest() throws Exception {
        CompletableFuture<Void> future = service.insertWhatAnimalQuestions();
        future.join();
        verify(questionService).saveAllQuestions(any());
        verify(questionService).saveAllAnswers(any());
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    void insertAnimalScientificNameQuestionsTest() throws Exception {
        CompletableFuture<Void> future = service.insertAnimalScientificNameQuestions();
        future.join();
        verify(questionService).saveAllQuestions(any());
        verify(questionService).saveAllAnswers(any());
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    void insertAnimalLifespanQuestionsTest() throws Exception {
        CompletableFuture<Void> future = service.insertAnimalLifespanQuestions();
        future.join();
        verify(questionService).saveAllQuestions(any());
        verify(questionService).saveAllAnswers(any());
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    void initTest() throws Exception {
        service.init();
        verify(questionService).eraseAll();
        verify(questionService, atLeast(11)).saveAllQuestions(any());
        verify(questionService, atLeast(11)).saveAllAnswers(any());
    }
}