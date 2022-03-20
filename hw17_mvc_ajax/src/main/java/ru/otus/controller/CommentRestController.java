package ru.otus.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.controller.mapper.CommentMapper;
import ru.otus.dto.CommentDTO;
import ru.otus.service.CommentService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/comments")
public class CommentRestController {
    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Validated
    @PostMapping
    public ResponseEntity<CommentDTO> create(@Valid @RequestBody CommentDTO comment) {
        final var createdComment = commentService.create(
                comment.getBookId(),
                comment.getTitle(),
                comment.getBody(),
                comment.getUserName()
        );
        return ResponseEntity.ok(CommentMapper.fromEntity(createdComment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<CommentDTO>> findByBookId(@PathVariable("id") long bookId) {
        final var comments = commentService.findByBookId(bookId).stream()
                .map(CommentMapper::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        commentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
