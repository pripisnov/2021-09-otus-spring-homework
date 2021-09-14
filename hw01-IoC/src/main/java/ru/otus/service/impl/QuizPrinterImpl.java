package ru.otus.service.impl;

import org.springframework.lang.NonNull;
import ru.otus.core.Question;
import ru.otus.service.QuizPrinter;

import java.util.List;

public class QuizPrinterImpl implements QuizPrinter {
    @Override
    public void printAll(@NonNull List<Question> questions) {
        System.out.println("\nAll questions by quiz:");
        for(int i = 0; i < questions.size(); ++i) {
            System.out.printf(
                    "Question №%s by category \"%s\": %s?\n",
                    i + 1,
                    questions.get(i).getCategory().getFriendlyName(),
                    questions.get(i).getBody()
            );
        }
    }
}
