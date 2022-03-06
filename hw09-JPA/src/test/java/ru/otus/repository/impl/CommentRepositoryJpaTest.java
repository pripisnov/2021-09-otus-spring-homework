package ru.otus.repository.impl;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.entity.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями к книгам")
@DataJpaTest
@Import(CommentRepositoryJpa.class)
class CommentRepositoryJpaTest {
    private static final long BOOK_ID = 1L;
    private static final long COMMENT_ID = 1L;
    private static final long NEW_COMMENT_ID = COMMENT_ID + 1;

    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Создание комментария")
    @Test
    void create() {
        val newComment = new Comment(
                0,
                "новый титул",
                "новое тело комментария",
                "какой-то юзер",
                BOOK_ID
        );

        commentRepositoryJpa.create(newComment);

        assertThat(testEntityManager.find(Comment.class, NEW_COMMENT_ID))
                .usingRecursiveComparison().isEqualTo(newComment);

    }

    @DisplayName("Удаление комментария по id")
    @Test
    void deleteById() {
        assertThat(testEntityManager.find(Comment.class, COMMENT_ID)).isNotNull();

        commentRepositoryJpa.deleteById(BOOK_ID);

        testEntityManager.getEntityManager().clear();

        assertThat(testEntityManager.find(Comment.class, COMMENT_ID)).isNull();
    }
}