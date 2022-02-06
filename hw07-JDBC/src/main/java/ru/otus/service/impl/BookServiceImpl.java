package ru.otus.service.impl;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.otus.dao.AuthorDAO;
import ru.otus.dao.BookDAO;
import ru.otus.dao.GenreDAO;
import ru.otus.entity.Book;
import ru.otus.service.BookService;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookDAO bookDAO;
    private final AuthorDAO authorDAO;
    private final GenreDAO genreDAO;

    public BookServiceImpl(BookDAO bookDAO, AuthorDAO authorDAO, GenreDAO genreDAO) {
        this.bookDAO = bookDAO;
        this.authorDAO = authorDAO;
        this.genreDAO = genreDAO;
    }

    @Override
    public List<Book> find(String name) {
        final var books = bookDAO.findByName(name);
        if (CollectionUtils.isEmpty(books)) {
            throw new RuntimeException("не найдены книги с именем " + name);
        }
        return books;
    }

    @Override
    public Book find(long id) {
        return bookDAO.findById(id).orElseThrow(() -> new RuntimeException("не найдена книга с id " + id));
    }

    @Override
    public Book create(String name, String authorName, String genreName) {
        Preconditions.checkArgument(
                StringUtils.isNoneEmpty(name),
                "имя книги не может быть пустым"
        );
        Preconditions.checkArgument(
                StringUtils.isNoneEmpty(authorName),
                "имя автора не может быть пустым"
        );
        Preconditions.checkArgument(
                StringUtils.isNoneEmpty(genreName),
                "имя жанра не может быть пустым"
        );
        final var author = authorDAO.findByName(authorName)
                .orElseThrow(() -> new RuntimeException("отсутствует автор с именем " + authorName));
        final var genre = genreDAO.findByName(genreName)
                .orElseThrow(() -> new RuntimeException("отсутствует жанр с именем " + genreName));
        final var id = bookDAO.save(new Book(name, author, genre));
        return bookDAO.findById(id).orElseThrow(
                () -> new RuntimeException(
                        String.format("Не удалось сохранить книгу: имя=%s, автор=%s, жанр=%s",
                                name,
                                authorName,
                                genreName)
                )
        );
    }

    @Override
    public Book update(long id, String name, String authorName, String genreName) {
        Preconditions.checkArgument(
                StringUtils.isNoneEmpty(name),
                "имя книги не может быть пустым"
        );
        final var book = bookDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Отсутствует книга с id=" + id));
        book.setName(name);
        if (StringUtils.isNoneEmpty(authorName) && !Objects.equals(book.getAuthor().getName(), authorName)) {
            final var author = authorDAO.findByName(authorName)
                    .orElseThrow(() -> new RuntimeException("отсутствует автор с именем " + authorName));
            book.setAuthor(author);
        }
        if (StringUtils.isNoneEmpty(genreName) && !Objects.equals(book.getGenre().getName(), genreName)) {
            final var genre = genreDAO.findByName(genreName)
                    .orElseThrow(() -> new RuntimeException("отсутствует жанр с именем " + genreName));
            book.setGenre(genre);
        }
        bookDAO.update(book);
        return bookDAO.findById(id).orElseThrow(
                () -> new RuntimeException(
                        String.format("Не удалось обновить книгу: имя=%s, автор=%s, жанр=%s",
                                name,
                                authorName,
                                genreName)
                )
        );
    }

    @Override
    public void delete(long id) {
        bookDAO.delete(id);
    }
}
