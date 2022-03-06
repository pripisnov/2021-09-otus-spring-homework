DROP TABLE IF EXISTS authors;
CREATE TABLE authors
(
    author_id   bigserial,
    author_name varchar(255),
    primary key (author_id)
);

DROP TABLE IF EXISTS genres;
CREATE TABLE genres
(
    genre_id   bigserial,
    genre_name varchar(255),
    primary key (genre_id)
);

DROP TABLE IF EXISTS books;
CREATE TABLE books
(
    book_id   bigserial,
    book_name varchar(255),
    author_id bigint,
    genre_id  bigint,
    primary key (book_id),
    foreign key (author_id) references authors(author_id),
    foreign key (genre_id) references genres(genre_id)
);

DROP TABLE IF EXISTS book_comments;
CREATE TABLE book_comments
(
    comment_id bigserial,
    comment_title varchar(100),
    comment_body varchar(4000),
    user_name varchar(255),
    book_id bigint references books(book_id) on delete cascade,
    primary key (comment_id)
);