package ru.otus.service;

import ru.otus.entity.Comment;

public interface CommentService {
    Comment create(long book, String title, String body, String user);
    void deleteById(long id);
}
