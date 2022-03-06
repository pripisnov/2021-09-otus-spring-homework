package ru.otus.repository;

import ru.otus.entity.Author;

import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> findById(long id);
    Optional<Author> findByName(String name);
}
