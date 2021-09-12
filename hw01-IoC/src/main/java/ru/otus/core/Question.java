package ru.otus.core;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class Question {
    private final String body;
    private final QuestionCategory category;

    public Question(@NonNull String body, @NonNull QuestionCategory category) {
        Preconditions.checkArgument(
                StringUtils.isNoneEmpty(body),
                "Тело вопроса не может быть null или пустым"
        );
        Preconditions.checkNotNull(
                category,
                "Категория вопроса не может быть null"
        );
        this.body = body;
        this.category = category;
    }

    public String getBody() {
        return body;
    }

    public QuestionCategory getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(body, question.body) && category == question.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(body, category);
    }
}
