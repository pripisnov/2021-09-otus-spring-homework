package ru.otus.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.exception.QuizRuntimeException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест класса QuestionMapper")
class QuestionMapperTest {
    private final static String ROW = "Сколько бит в байте:COMPUTER_SCIENCE";
    private final static String BODY = "Сколько бит в байте";
    private final static QuestionCategory QUESTION_CATEGORY = QuestionCategory.COMPUTER_SCIENCE;

    @DisplayName("Тест выброса IllegalArgumentException при пустой строке")
    @Test
    void toQuestionWithEmptyRowFail() {
        assertThrows(IllegalArgumentException.class, () -> QuestionMapper.toQuestion(""));
    }

    @DisplayName("Тест выброса IllegalArgumentException при пустой строке")
    @Test
    void toQuestionWithNullRowFail() {
        assertThrows(IllegalArgumentException.class, () -> QuestionMapper.toQuestion(null));
    }

    @DisplayName("Тест выброса QuizRuntimeException при неправильном формате строке")
    @Test
    void toQuestionWithUncorrectedRowFail() {
        assertThrows(QuizRuntimeException.class, () -> QuestionMapper.toQuestion("привет!"));
    }

    @DisplayName("Тест метода для маппинга строки в объект Question")
    @Test
    void toQuestionSuccess() {
        final var testQuestion = new Question(BODY, QUESTION_CATEGORY);
        final var mappedQuestion = QuestionMapper.toQuestion(ROW);

        assertEquals(testQuestion, mappedQuestion);
    }
}