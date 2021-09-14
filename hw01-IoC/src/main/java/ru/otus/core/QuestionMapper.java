package ru.otus.core;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import ru.otus.core.exception.QuizRuntimeException;

public class QuestionMapper {

    private QuestionMapper() {
        //util
    }
    public static Question toQuestion(final String row) {
        Preconditions.checkArgument(
                StringUtils.isNoneBlank(row),
                "String should be not blank"
        );

        String[] fields = row.split(QuizProperty.DELIMITER);

        if(fields.length != QuizProperty.FIELD_COUNT) {
            throw new QuizRuntimeException("Not formatted string");
        }

        final String body = fields[0];
        final QuestionCategory category = QuestionCategory.parseName(fields[1]);

        return new Question(body, category);
    }
}
