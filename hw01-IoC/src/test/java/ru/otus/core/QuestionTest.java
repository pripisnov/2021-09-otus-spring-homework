package ru.otus.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест класса Question")
class QuestionTest {
    private final static String BODY = "Сколько бит в байте";
    private final static QuestionCategory QUESTION_CATEGORY = QuestionCategory.COMPUTER_SCIENCE;

    @DisplayName("Тест выброса IllegalArgumentException при передаче в конструктор пустого body")
    @Test
    void createQuestionWithEmptyBodyFail() {
        assertThrows(IllegalArgumentException.class, () -> new Question("", QUESTION_CATEGORY));
    }

    @DisplayName("Тест выброса IllegalArgumentException при передаче в конструктор null body")
    @Test
    void createQuestionWithNullBodyFail() {
        assertThrows(IllegalArgumentException.class, () -> new Question(null, QUESTION_CATEGORY));
    }

    @DisplayName("Тест выброса NullPointerException при передаче в конструктор null category")
    @Test
    void createQuestionWithNullCategoryFail() {
        assertThrows(NullPointerException.class, () -> new Question(BODY, null));
    }

    @DisplayName("Тест корректной работы конструктора")
    @Test
    void createQuestionSuccess() {
        final var question = new Question(BODY, QUESTION_CATEGORY);

        assertAll(
                () -> assertEquals(BODY, question.getBody()),
                () -> assertEquals(QUESTION_CATEGORY, question.getCategory())
        );
    }
}