package ru.otus.service.impl;

import org.springframework.core.io.Resource;
import ru.otus.core.Question;
import ru.otus.core.QuestionMapper;
import ru.otus.core.exception.QuizRuntimeException;
import ru.otus.service.QuestionReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuestionReaderImpl implements QuestionReader {

    private final Resource resource;

    public QuestionReaderImpl(Resource resource) {
        this.resource = resource;
    }

    @Override
    public List<Question> readByResource() {
        final List<Question> questions = new ArrayList<>();
        try (
                final var resourceInputStream = resource.getInputStream();
                final var bufferedReader = new BufferedReader(new InputStreamReader(resourceInputStream))) {
            bufferedReader.lines().forEach(
                    line ->  questions.add(QuestionMapper.toQuestion(line))
            );
        } catch (Exception ex) {
            throw new QuizRuntimeException("Non read resource", ex);
        }
        return questions;
    }
}
