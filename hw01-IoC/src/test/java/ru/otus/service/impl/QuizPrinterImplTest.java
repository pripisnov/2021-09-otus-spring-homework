package ru.otus.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.Question;
import ru.otus.core.QuestionCategory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тест класса QuizPrinterImpl")
class QuizPrinterImplTest {
    private final static String OUTPUT_STRING =
            "All questions by quiz:Question №1 by category \"Java core\": Who is the Java creator?";

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    void setOut() {
        System.setOut(new PrintStream(output));
    }

    @DisplayName("Тест печати всех вопросов в консоль")
    @Test
    void printAll() {
        final var question = new Question("Who is the Java creator", QuestionCategory.JAVA_CORE);
        new QuizPrinterImpl().printAll(Collections.singletonList(question));
        assertEquals(
                output.toString().replace("\n", "").replace("\r", ""),
                OUTPUT_STRING
        );
    }
}