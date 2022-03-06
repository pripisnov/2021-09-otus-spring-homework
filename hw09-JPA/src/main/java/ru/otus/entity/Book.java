package ru.otus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "book_name", nullable = false)
    private String name;

    @ManyToOne()
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne()
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Fetch(FetchMode.JOIN)
    //@BatchSize(size = 100)
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private Set<Comment> comments;

    public Book(String name, Author author, Genre genre) {
        this.name = name;
        this.author = author;
        this.genre = genre;
    }
}
