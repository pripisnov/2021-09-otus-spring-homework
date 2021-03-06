package ru.otus.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.entity.Author;
import ru.otus.entity.Book;
import ru.otus.entity.Genre;
import ru.otus.exception.NotFoundException;
import ru.otus.repository.AuthorRepository;
import ru.otus.repository.BookRepository;
import ru.otus.repository.GenreRepository;
import ru.otus.service.BookService;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Сервис для работы с книгами")
@SpringBootTest(classes = {BookServiceImpl.class})
class BookServiceImplTest {
    private static final long ID = 1;
    private static final String NAME = "Песнь льда и Пламени";
    private static final String NEW_NAME = "Игра Престолов";
    private static final Author AUTHOR = new Author(2, "Дж.Дж. Мартин");
    private static final Genre GENRE = new Genre(2, "Фэнтези");
    private static final Book TEST_BOOK = new Book(ID, NAME, AUTHOR, GENRE);
    private static final Book NEW_TEST_BOOK = new Book(ID, NEW_NAME, AUTHOR, GENRE);

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private BookService bookService;

    @BeforeEach
    void setUp() {
        Mockito.when(bookRepository.findByName(TEST_BOOK.getName()))
                .thenReturn(Collections.singletonList(TEST_BOOK));
        Mockito.when(bookRepository.findById(TEST_BOOK.getId()))
                .thenReturn(Optional.of(TEST_BOOK));
        Mockito.when(bookRepository.save(Mockito.any(Book.class)))
                .thenReturn(TEST_BOOK);
        Mockito.when(authorRepository.findByName(AUTHOR.getName()))
                .thenReturn(Optional.of(AUTHOR));
        Mockito.when(genreRepository.findByName(GENRE.getName()))
                .thenReturn(Optional.of(GENRE));
    }

    @DisplayName("Поиск книги по имени (успех)")
    @Test
    void findByName_Success() {
        final var books = bookService.findByName(TEST_BOOK.getName());
        assertThat(books).usingRecursiveComparison().isEqualTo(Collections.singletonList(TEST_BOOK));
    }

    @DisplayName("Поиск книги по ID (успех)")
    @Test
    void findById_Success() {
        final var book = bookService.findById(TEST_BOOK.getId()).orElseThrow();
        assertThat(book).usingRecursiveComparison().isEqualTo(TEST_BOOK);
    }

    @DisplayName("Создание книги (успех)")
    @Test
    void create_Success() {
        final var book = bookService.create(TEST_BOOK.getName(), AUTHOR.getName(), GENRE.getName());
        assertThat(book).usingRecursiveComparison().isEqualTo(TEST_BOOK);
    }

    @DisplayName("Создание книги (исключение: пустое имя книги, автора или жанра)")
    @Test
    void create_NamesFail() {
        assertThatThrownBy(() -> bookService.create("", AUTHOR.getName(), GENRE.getName()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> bookService.create(TEST_BOOK.getName(), "", GENRE.getName()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> bookService.create(TEST_BOOK.getName(), AUTHOR.getName(), ""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Создание книги (исключение: неверное имя автора)")
    @Test
    void create_AuthorFail() {
        assertThatThrownBy(() -> bookService.create(TEST_BOOK.getName(), "А.С. Пушкин", GENRE.getName()))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Создание книги (исключение: неверное имя жанра)")
    @Test
    void create_GenreFail() {
        assertThatThrownBy(() -> bookService.create(TEST_BOOK.getName(), AUTHOR.getName(), "Сказка"))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Обновление книги (успех)")
    @Test
    void update_Success() {
        Mockito.when(bookRepository.save(NEW_TEST_BOOK))
                .thenReturn(NEW_TEST_BOOK);
        final var updatedBook = bookService.update(NEW_TEST_BOOK.getId(), NEW_NAME, AUTHOR.getName(), GENRE.getName());
        assertThat(NEW_TEST_BOOK).usingRecursiveComparison().isEqualTo(updatedBook);
    }

    @DisplayName("Обновление книги (успех: без автора и жанра)")
    @Test
    void update_OnlyBookNameSuccess() {
        Mockito.when(bookRepository.save(NEW_TEST_BOOK))
                .thenReturn(NEW_TEST_BOOK);
        final var updatedBook = bookService.update(NEW_TEST_BOOK.getId(), NEW_NAME, null, null);
        assertThat(NEW_TEST_BOOK).usingRecursiveComparison().isEqualTo(updatedBook);
    }

    @DisplayName("Обновление книги (исключение: неверное новое имя книги)")
    @Test
    void update_NameFail() {
        assertThatThrownBy(() -> bookService.update(ID, "", AUTHOR.getName(), GENRE.getName()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Обновление книги (исключение: неверное имя автора)")
    @Test
    void update_AuthorFail() {
        assertThatThrownBy(() -> bookService.update(ID, NEW_NAME, "А.С. Пушкин", GENRE.getName()))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Обновление книги (исключение: неверное имя жанра)")
    @Test
    void update_GenreFail() {
        assertThatThrownBy(() -> bookService.update(ID, NEW_NAME, AUTHOR.getName(), "Сказка"))
                .isInstanceOf(NotFoundException.class);
    }
}