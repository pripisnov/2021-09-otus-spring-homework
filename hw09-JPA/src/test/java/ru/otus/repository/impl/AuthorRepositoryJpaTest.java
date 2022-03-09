package ru.otus.repository.impl;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.entity.Author;

import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами книг")
@DataJpaTest
@Import(AuthorRepositoryJpa.class)
class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepositoryJpa authorRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Поиск автора по id")
    @Test
    void findById() {
        val optionalAuthor = authorRepositoryJpa.findById(1L);
        val expectedAuthor = testEntityManager.find(Author.class, 1L);
        assertThat(optionalAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("Поиск автора по имени")
    @Test
    void findByName() {
        val authorName = "А.С. Пушкин";
        val author = authorRepositoryJpa.findByName(authorName).orElseThrow();

        TypedQuery<Author> query = testEntityManager.getEntityManager().createQuery(
                "select a from Author a where a.name = '" + authorName + "'",
                Author.class
        );
        val resultList = query.getResultList();
        assertThat(resultList).isNotNull().doesNotContainNull().isNotEmpty();
        assertThat(author).usingRecursiveComparison().isEqualTo(resultList.get(0));
    }
}