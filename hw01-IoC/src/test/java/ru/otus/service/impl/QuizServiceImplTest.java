package ru.otus.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.core.Question;
import ru.otus.core.QuestionCategory;
import ru.otus.service.QuestionReader;
import ru.otus.service.QuizPrinter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.doAnswer;

@DisplayName("Тест класса QuizServiceImpl")
class QuizServiceImplTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    void setOut() {
        System.setOut(new PrintStream(output));
    }

    @DisplayName("Тест печати всех вопросов")
    @Test
    void printAllQuestions() {
        final var question = new Question("Вы любите java", QuestionCategory.JAVA_CORE);
        //mock questionReader
        final var questionReader = Mockito.mock(QuestionReader.class);
        Mockito.when(questionReader.readByResource())
                .thenReturn(Collections.singletonList(question));

        //mock quizPrinter
        final var quizPrinter = Mockito.mock(QuizPrinter.class);
        doAnswer(invocation -> {
            List<Question> arg0 = invocation.getArgument(0);
            System.out.print(arg0.get(0).getBody());
            return null;
        }).when(quizPrinter).printAll(anyList());

        //quizService
        final var quizService = new QuizServiceImpl(questionReader, quizPrinter);
        quizService.printAllQuestions();

        assertEquals(question.getBody(), output.toString());
    }
}