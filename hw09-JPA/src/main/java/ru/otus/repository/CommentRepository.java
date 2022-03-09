package ru.otus.repository;

import ru.otus.entity.Comment;

import java.util.List;

public interface CommentRepository {
    Comment create(Comment comment);
    List<Comment> findByBookId(long bookId);
    void deleteById(long id);
}
