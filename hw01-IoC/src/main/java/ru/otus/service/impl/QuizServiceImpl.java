package ru.otus.service.impl;

import ru.otus.service.QuestionReader;
import ru.otus.service.QuizPrinter;
import ru.otus.service.QuizService;

public class QuizServiceImpl implements QuizService {
    private final QuestionReader reader;
    private final QuizPrinter printer;

    public QuizServiceImpl(QuestionReader reader, QuizPrinter printer) {
        this.reader = reader;
        this.printer = printer;
    }

    @Override
    public void printAllQuestions() {
        final var questions = reader.readByResource();
        printer.printAll(questions);
    }
}
