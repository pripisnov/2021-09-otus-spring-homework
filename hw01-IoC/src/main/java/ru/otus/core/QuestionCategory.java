package ru.otus.core;

import ru.otus.core.exception.QuizRuntimeException;

import java.util.Arrays;
import java.util.Objects;

public enum QuestionCategory {
    COMPUTER_SCIENCE("Информатика"),
    JAVA_CORE("Java основы")
    ;

    private final String friendlyName;

    QuestionCategory(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public static QuestionCategory parseName(String name) {
        return Arrays.stream(values()).filter(v -> Objects.equals(v.name(), name)).findFirst().orElseThrow(
                () -> new QuizRuntimeException("Отсутствует категория вопроса с именем " + name)
        );
    }
}
