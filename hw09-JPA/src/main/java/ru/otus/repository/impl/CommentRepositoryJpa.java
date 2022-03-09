package ru.otus.repository.impl;

import org.springframework.stereotype.Repository;
import ru.otus.entity.Comment;
import ru.otus.repository.CommentRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public CommentRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Comment create(Comment comment) {
        entityManager.persist(comment);
        return comment;
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c where c.book.id = :bookId",
                Comment.class
        );
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        Query query = entityManager.createQuery(
                "delete from Comment c where c.id = :id"
        );
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
