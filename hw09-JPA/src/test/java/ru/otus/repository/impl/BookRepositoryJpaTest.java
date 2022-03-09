package ru.otus.repository.impl;

import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.entity.Author;
import ru.otus.entity.Book;
import ru.otus.entity.Genre;

import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами")
@DataJpaTest
@Import(BookRepositoryJpa.class)
class BookRepositoryJpaTest {

    private static final long BOOK_ID = 1L;
    private static final int BOOK_COUNT = 2;
    private static final long NEW_BOOK_ID = BOOK_COUNT + 1;
    private static final String BOOK_NAME = "Песнь льда и Пламени";
    private static final Author AUTHOR = new Author(2, "Дж.Дж. Мартин");
    private static final Genre GENRE = new Genre(2, "Фэнтези");

    @Autowired
    private BookRepositoryJpa bookRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Поиск книги по id")
    @Test
    void findById() {
        val optionalBook = bookRepositoryJpa.findById(BOOK_ID);
        val expectedBook = testEntityManager.find(Book.class, BOOK_ID);
        assertThat(optionalBook).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Поиск книги по имени")
    @Test
    void findByName() {
        val books = bookRepositoryJpa.findByName(BOOK_NAME);
        assertThat(books).isNotNull().doesNotContainNull().isNotEmpty();

        TypedQuery<Book> query = testEntityManager.getEntityManager().createQuery(
                "select b from Book b where b.name = '" + BOOK_NAME + "'",
                Book.class
        );
        val resultList = query.getResultList();
        assertThat(resultList).isNotNull().doesNotContainNull().isNotEmpty();
        assertThat(books.get(0)).usingRecursiveComparison().isEqualTo(resultList.get(0));
    }

    @DisplayName("Создание книги")
    @Test
    void create() {
        val newBook = new Book(0, "Игра Престолов", AUTHOR, GENRE);

        bookRepositoryJpa.create(newBook);

        assertThat(testEntityManager.find(Book.class, NEW_BOOK_ID)).usingRecursiveComparison().isEqualTo(newBook);
    }


    @DisplayName("Обновление книги")
    @Test
    void update() {
        String newName = "Игра Престолов";
        val newBook = new Book(BOOK_ID, newName, AUTHOR, GENRE);

        assertThat(testEntityManager.find(Book.class, BOOK_ID)).usingRecursiveComparison().isNotEqualTo(newBook);

        bookRepositoryJpa.update(newBook);

        testEntityManager.getEntityManager().flush();
        testEntityManager.getEntityManager().clear();

        assertThat(testEntityManager.find(Book.class, BOOK_ID)).usingRecursiveComparison().isEqualTo(newBook);
    }


    @DisplayName("Удаление книги по id")
    @Test
    void delete() {
        assertThat(testEntityManager.find(Book.class, BOOK_ID)).isNotNull();

        bookRepositoryJpa.deleteById(BOOK_ID);

        testEntityManager.getEntityManager().clear();

        assertThat(testEntityManager.find(Book.class, BOOK_ID)).isNull();
    }

    @DisplayName("Найти все книги")
    @Test
    void findAll() {
        val books = bookRepositoryJpa.findAll();
        assertThat(books).isNotNull().hasSize(BOOK_COUNT)
                .allMatch(b -> StringUtils.isNoneEmpty(b.getName()))
                .allMatch(b -> b.getAuthor() != null)
                .allMatch(b -> b.getGenre() != null);
    }
}