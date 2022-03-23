package ru.otus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.controller.mapper.CommentMapper;
import ru.otus.dto.CommentDTO;
import ru.otus.entity.Author;
import ru.otus.entity.Book;
import ru.otus.entity.Comment;
import ru.otus.entity.Genre;
import ru.otus.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тесты rest-контроллера для работы с комментариями к книгам")
@WebMvcTest(CommentRestController.class)
class CommentRestControllerTest {
    private static final long ID = 1L;
    private static final String NAME = "Песнь льда и Пламени";
    private static final Author AUTHOR = new Author(2, "Дж.Дж. Мартин");
    private static final Genre GENRE = new Genre(2, "Фэнтези");
    private static final Book TEST_BOOK = new Book(ID, NAME, AUTHOR, GENRE);
    private static final Comment COMMENT =
            new Comment(1, "Титул", "Тело коммента", "юзер", TEST_BOOK);
    private static final Comment NEW_COMMENT =
            new Comment(2, "Титул2", "Тело коммента2", "юзер2", TEST_BOOK);
    private static final List<Comment> COMMENTS = Lists.newArrayList(COMMENT, NEW_COMMENT);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;


    @DisplayName("Поиск всех комментариев по id книги")
    @Test
    public void findAll() throws Exception {
        given(commentService.findByBookId(1L)).willReturn(COMMENTS);

        List<CommentDTO> expectedResult = COMMENTS.stream()
                .map(CommentMapper::fromEntity).collect(Collectors.toList());

        mockMvc.perform(get("/comments/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }

    @DisplayName("Создание комментария")
    @Test
    public void create() throws Exception {
        given(
                commentService.create(
                        TEST_BOOK.getId(),
                        COMMENT.getCommentTitle(),
                        COMMENT.getCommentBody(),
                        COMMENT.getUserName()
                )
        ).willReturn(COMMENT);
        String expectedResult = mapper.writeValueAsString(CommentMapper.fromEntity(COMMENT));

        mockMvc.perform(post("/comments").contentType(APPLICATION_JSON).content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @DisplayName("Удаление комментария по id")
    @Test
    public void deleteById() throws Exception {
        mockMvc.perform(delete("/comments/1"))
                .andExpect(status().isOk());
        verify(commentService, times(1)).deleteById(1L);
    }
}