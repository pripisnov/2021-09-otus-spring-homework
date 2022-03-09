package ru.otus.repository.impl;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.entity.Author;
import ru.otus.entity.Book;
import ru.otus.entity.Comment;
import ru.otus.entity.Genre;

import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями к книгам")
@DataJpaTest
@Import(CommentRepositoryJpa.class)
class CommentRepositoryJpaTest {
    private static final long BOOK_ID = 1L;
    private static final int COUNT = 2;
    private static final long COMMENT_ID = 1L;
    private static final long NEW_COMMENT_ID = COUNT + 1;
    private static final String BOOK_NAME = "Песнь льда и Пламени";
    private static final Author AUTHOR = new Author(2, "Дж.Дж. Мартин");
    private static final Genre GENRE = new Genre(2, "Фэнтези");
    private static final Book TEST_BOOK = new Book(1, BOOK_NAME, AUTHOR, GENRE);

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
                TEST_BOOK
        );

        commentRepositoryJpa.create(newComment);

        assertThat(testEntityManager.find(Comment.class, NEW_COMMENT_ID))
                .usingRecursiveComparison().isEqualTo(newComment);

    }

    @DisplayName("Удаление комментария по id")
    @Test
    void deleteById() {
        assertThat(testEntityManager.find(Comment.class, COMMENT_ID)).isNotNull();

        commentRepositoryJpa.deleteById(COMMENT_ID);

        testEntityManager.getEntityManager().clear();

        assertThat(testEntityManager.find(Comment.class, COMMENT_ID)).isNull();
    }

    @DisplayName("Найти комментарии по id книги")
    @Test
    void findByBookId() {
        val comments = commentRepositoryJpa.findByBookId(BOOK_ID);
        assertThat(comments).isNotNull().doesNotContainNull().isNotEmpty();

        TypedQuery<Comment> query = testEntityManager.getEntityManager().createQuery(
                "select c from Comment c where c.book =" + BOOK_ID,
                Comment.class
        );
        val resultList = query.getResultList();
        assertThat(resultList).isNotNull().doesNotContainNull().isNotEmpty();
        assertThat(comments).usingRecursiveComparison().isEqualTo(resultList);
    }
}