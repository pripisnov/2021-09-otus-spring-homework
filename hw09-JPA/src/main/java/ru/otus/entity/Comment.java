package ru.otus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
    comment_id bigserial,
    comment_title varchar(100),
    comment_body varchar(4000),
    user_name varchar(255),
    book_id bigint references books(book_id) on delete cascade,
    primary key (comment_id)
 */

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book_comments")
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "comment_title")
    private String commentTitle;

    @Column(name = "comment_body")
    private String commentBody;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "book_id")
    private long book;
}
