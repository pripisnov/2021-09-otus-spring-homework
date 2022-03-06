package ru.otus.repository.impl;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.entity.Genre;

import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами книг")
@DataJpaTest
@Import(GenreRepositoryJpa.class)
class GenreRepositoryJpaTest {

    @Autowired
    private GenreRepositoryJpa genreRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Поиск жанра по id")
    @Test
    void findById() {
        val optionalGenre = genreRepositoryJpa.findById(1L);
        val expectedGenre = testEntityManager.find(Genre.class, 1L);
        assertThat(optionalGenre).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Поиск жанра по имени")
    @Test
    void findByName() {
        val authorName = "Сказка";
        val genre = genreRepositoryJpa.findByName(authorName).orElseThrow();

        TypedQuery<Genre> query = testEntityManager.getEntityManager().createQuery(
                "select g from Genre g where g.name = '" + authorName + "'",
                Genre.class
        );
        val resultList = query.getResultList();
        assertThat(resultList).isNotNull().doesNotContainNull().isNotEmpty();
        assertThat(genre).usingRecursiveComparison().isEqualTo(resultList.get(0));
    }
}