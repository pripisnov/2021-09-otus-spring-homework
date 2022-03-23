package ru.otus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.controller.mapper.BookMapper;
import ru.otus.dto.BookDTO;
import ru.otus.entity.Author;
import ru.otus.entity.Book;
import ru.otus.entity.Genre;
import ru.otus.service.BookService;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тесты rest-контроллера для работы с книгами")
@WebMvcTest(BookRestController.class)
class BookRestControllerTest {
    private static final long ID = 1;
    private static final String NAME = "Песнь льда и Пламени";
    private static final String NEW_NAME = "Игра Престолов";
    private static final Author AUTHOR = new Author(2, "Дж.Дж. Мартин");
    private static final Genre GENRE = new Genre(2, "Фэнтези");
    private static final Book TEST_BOOK = new Book(ID, NAME, AUTHOR, GENRE);
    private static final Book NEW_TEST_BOOK = new Book(ID + 1, NEW_NAME, AUTHOR, GENRE);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @DisplayName("Поиск всех книг")
    @Test
    public void findAll() throws Exception {
        List<Book> books = List.of(TEST_BOOK, NEW_TEST_BOOK);
        given(bookService.findAll()).willReturn(books);

        List<BookDTO> expectedResult = books.stream()
                .map(BookMapper::fromEntity).collect(Collectors.toList());

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }

    @DisplayName("Создание книги")
    @Test
    public void create() throws Exception {
        given(
                bookService.create(
                        TEST_BOOK.getName(),
                        TEST_BOOK.getAuthor().getName(),
                        TEST_BOOK.getGenre().getName()
                )
        ).willReturn(TEST_BOOK);
        String expectedResult = mapper.writeValueAsString(BookMapper.fromEntity(TEST_BOOK));

        mockMvc.perform(post("/books").contentType(APPLICATION_JSON).content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @DisplayName("Обновление книги")
    @Test
    public void update() throws Exception {
        given(
                bookService.update(
                        TEST_BOOK.getId(),
                        TEST_BOOK.getName(),
                        TEST_BOOK.getAuthor().getName(),
                        TEST_BOOK.getGenre().getName()
                )
        ).willReturn(TEST_BOOK);
        String expectedResult = mapper.writeValueAsString(BookMapper.fromEntity(TEST_BOOK));

        mockMvc.perform(put("/books").contentType(APPLICATION_JSON).content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @DisplayName("Удаление книги по id")
    @Test
    public void deleteById() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isOk());
        verify(bookService, times(1)).delete(1L);
    }
}