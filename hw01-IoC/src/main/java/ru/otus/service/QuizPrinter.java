package ru.otus.service;

import ru.otus.core.Question;

import java.util.List;

public interface QuizPrinter {
    void printAll(List<Question> questions);
}
