package ru.otus.repository;

import ru.otus.entity.Genre;

import java.util.Optional;

public interface GenreRepository {
    Optional<Genre> findById(long id);
    Optional<Genre> findByName(String name);
}
