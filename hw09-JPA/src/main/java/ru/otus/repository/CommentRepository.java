package ru.otus.repository;

import ru.otus.entity.Comment;

public interface CommentRepository {
    Comment create(Comment comment);
    void deleteById(long id);
}
