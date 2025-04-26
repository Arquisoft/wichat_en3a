package com.uniovi.wichatwebapp.entitites;

import com.uniovi.wichatwebapp.entities.QuestionCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Fail.fail;

@ExtendWith(MockitoExtension.class)
public class QuestionCategoryTest {
    @Test
    void containsValuesTests() {
        List<QuestionCategory> categories = Arrays.stream(QuestionCategory.values()).toList();

        Assertions.assertTrue(categories.contains(QuestionCategory.SPORT));
        Assertions.assertTrue(categories.contains(QuestionCategory.GEOGRAPHY));
        Assertions.assertTrue(categories.contains(QuestionCategory.POP_CULTURE));
        Assertions.assertTrue(categories.contains(QuestionCategory.BIOLOGY));
    }

    @Test
    void valueOfTest() {
        Assertions.assertEquals(QuestionCategory.SPORT, QuestionCategory.valueOf("SPORT"));
        Assertions.assertEquals(QuestionCategory.GEOGRAPHY, QuestionCategory.valueOf("GEOGRAPHY"));
        Assertions.assertEquals(QuestionCategory.POP_CULTURE, QuestionCategory.valueOf("POP_CULTURE"));
        Assertions.assertEquals(QuestionCategory.BIOLOGY, QuestionCategory.valueOf("BIOLOGY"));
    }

    @Test
    void toStringTest() {
        Assertions.assertEquals("Sport", QuestionCategory.SPORT.toString());
        Assertions.assertEquals("Geography", QuestionCategory.GEOGRAPHY.toString());
        Assertions.assertEquals("Pop Culture", QuestionCategory.POP_CULTURE.toString());
        Assertions.assertEquals("Biology", QuestionCategory.BIOLOGY.toString());
    }
}
