package ru.otus.service;

import ru.otus.core.Question;

import java.util.List;

public interface QuestionReader {
    List<Question> readByResource();
}
