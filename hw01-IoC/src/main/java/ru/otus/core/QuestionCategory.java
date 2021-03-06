package ru.otus.core;

import ru.otus.core.exception.QuizRuntimeException;

import java.util.Arrays;
import java.util.Objects;

public enum QuestionCategory {
    COMPUTER_SCIENCE("Computer science"),
    JAVA_CORE("Java core")
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
                () -> new QuizRuntimeException("Not found question by " + name)
        );
    }
}
