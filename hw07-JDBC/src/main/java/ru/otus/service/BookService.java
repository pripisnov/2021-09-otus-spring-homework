package ru.otus.service;

import ru.otus.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> find(String name);
    Book find(long id);
    Book create(String name, String author, String genre);
    Book update(long id, String name, String author, String genre);
    void delete(long id);
}
