package ru.otus.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.exception.QuizRuntimeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тест класса QuestionCategory")
class QuestionCategoryTest {

    @DisplayName("Тест выброса исключения QuizRuntimeException при парсинге имени категории вопроса")
    @Test
    void parseNameFail() {
        assertThrows(QuizRuntimeException.class, () -> QuestionCategory.parseName("UNKNOWN_CATEGORY"));
    }

    @DisplayName("Тест успешного парсинга имени категории")
    @Test
    void parseNameSuccess() {
        assertEquals(QuestionCategory.JAVA_CORE, QuestionCategory.parseName(QuestionCategory.JAVA_CORE.name()));
    }
}