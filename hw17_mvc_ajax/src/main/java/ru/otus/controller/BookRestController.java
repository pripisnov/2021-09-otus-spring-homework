package ru.otus.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.controller.mapper.BookMapper;
import ru.otus.dto.BookDTO;
import ru.otus.service.BookService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookRestController {
    private static final String ERROR_MESSAGE_PREFIX = "Ошибка при выполнении: ";

    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> findAll() {
        final var books = bookService.findAll().stream()
                .map(BookMapper::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    @Validated
    @PostMapping
    public ResponseEntity<BookDTO> create(@Valid @RequestBody BookDTO book) {
        final var cratedBook = bookService.create(
                book.getBookName(),
                book.getAuthorName(),
                book.getGenreName()
        );
        return ResponseEntity.ok(BookMapper.fromEntity(cratedBook));
    }

    @Validated
    @PutMapping
    public ResponseEntity<BookDTO> update(@Valid @RequestBody BookDTO bookDTO) {
        final var updatedBook = bookService.update(
                bookDTO.getBookId(),
                bookDTO.getBookName(),
                bookDTO.getAuthorName(),
                bookDTO.getGenreName()
        );
        return ResponseEntity.ok(BookMapper.fromEntity(updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        bookService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
