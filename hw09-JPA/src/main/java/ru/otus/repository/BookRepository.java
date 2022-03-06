package ru.otus.repository;

import ru.otus.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book create(Book book);
    List<Book> findByName(String name);
    Optional<Book> findById(long id);
    Book update(Book book);
    void deleteById(long id);
}
