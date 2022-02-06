package ru.otus.console;

import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.entity.Book;
import ru.otus.service.BookService;

import java.util.Collections;
import java.util.List;

@ShellComponent
public class UserCommand {
    private final BookService bookService;

    public UserCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @ShellMethod("Найти книгу")
    public List<Book> find(
            @ShellOption(defaultValue = "0") long id,
            @ShellOption(defaultValue = "") String name) {
        if (id > 0) {
            return Collections.singletonList(bookService.find(id));
        } else if (StringUtils.isNoneEmpty(name)) {
            return bookService.find(name);
        }
        throw new IllegalArgumentException("необходимо задать id или имя книги");
    }

    /**
     * Для создания в консоли используем следующую запись:
     * <p>create 'Сказка о рыбаке и рыбке' 'А.С. Пушкин' 'Сказка'</p>
     * Предполагаем, что у нас один автор и один жанр у книги
     */
    @ShellMethod("Создать книгу")
    public Book create(String name, String authorName, String genreName) {
        return bookService.create(name, authorName, genreName);
    }

    @ShellMethod("Обновить книгу")
    public Book update(
            long id,
            String name,
            @ShellOption(defaultValue = "") String authorName,
            @ShellOption(defaultValue = "") String genreName) {
        return bookService.update(id, name, authorName, genreName);
    }

    @ShellMethod("Удалить книгу")
    public String delete(long id) {
        bookService.delete(id);
        return "done.";
    }
}
