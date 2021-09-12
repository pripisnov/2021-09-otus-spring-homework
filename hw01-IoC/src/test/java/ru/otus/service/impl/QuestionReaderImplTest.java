package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import ru.otus.core.Question;
import ru.otus.core.QuestionCategory;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест класса QuestionReaderImpl")
class QuestionReaderImplTest {

    @DisplayName("Тест чтения вопросов из ресурса")
    @Test
    void readByResource() {
        final String row = "Сколько бит в байте:COMPUTER_SCIENCE";

        final var mockResource = new MockResource(new ByteArrayInputStream(row.getBytes()));
        final var questionReader = new QuestionReaderImpl(mockResource);
        final var questions = questionReader.readByResource();

        final var readQuestion = questions.get(0);
        assertEquals(new Question("Сколько бит в байте", QuestionCategory.COMPUTER_SCIENCE), readQuestion);
    }
}