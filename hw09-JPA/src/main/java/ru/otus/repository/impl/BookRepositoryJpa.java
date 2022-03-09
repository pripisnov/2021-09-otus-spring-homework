package ru.otus.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.entity.Book;
import ru.otus.repository.BookRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public BookRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Book create(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    public List<Book> findByName(String name) {
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b where b.name = :name",
                Book.class
        );
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        final var book = entityManager.find(Book.class, id);
        return Optional.ofNullable(book);
    }

    @Override
    public Book update(Book book) {
        return entityManager.merge(book);
    }

    @Override
    public void deleteById(long id) {
        Query query = entityManager.createQuery(
                "delete from Book b where b.id = :id"
        );
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b join fetch b.author join fetch b.genre",
                Book.class
        );
        return query.getResultList();
    }
}
