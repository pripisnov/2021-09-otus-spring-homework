package ru.otus.console;

import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.CollectionUtils;
import ru.otus.service.BookService;
import ru.otus.service.CommentService;

@ShellComponent
public class UserCommand {
    private static final String ERROR_MESSAGE_PREFIX = "Ошибка при выполнении команды: ";
    private static final String DONE_MESSAGE = "done.";

    private final BookService bookService;
    private final CommentService commentService;

    public UserCommand(BookService bookService, CommentService commentService) {
        this.bookService = bookService;
        this.commentService = commentService;
    }

    /**
     * Для поиска книги в консоли используем следующую запись:
     * <p>find 1</p>
     * <p>или</p>
     * <p>find --name 'Сказка о рыбаке и рыбке'</p>
     */
    @ShellMethod("Найти книгу")
    public String find(
            @ShellOption(defaultValue = "0") long id,
            @ShellOption(defaultValue = "") String name) {
        if (id > 0) {
            final var optionalBook = bookService.findById(id);
            if (optionalBook.isEmpty()) {
                return ERROR_MESSAGE_PREFIX + "не найдена книга с id " + id;
            }
            return optionalBook.get().toString();
        } else if (StringUtils.isNoneEmpty(name)) {
            final var books = bookService.findByName(name);
            if (CollectionUtils.isEmpty(books)) {
                return ERROR_MESSAGE_PREFIX + "не найдены книги с name " + name;
            }
            return books.toString();
        }
        return ERROR_MESSAGE_PREFIX + "необходимо задать id или имя книги";
    }

    /**
     * Для создания книги в консоли используем следующую запись:
     * <p>create 'Сказка о рыбаке и рыбке' 'А.С. Пушкин' 'Сказка'</p>
     * Предполагаем, что у нас один автор и один жанр у книги
     */
    @ShellMethod("Создать книгу")
    public String create(String name, String authorName, String genreName) {
        try {
            return bookService.create(name, authorName, genreName).toString();
        } catch (Exception ex) {
            return ERROR_MESSAGE_PREFIX + ex.getMessage();
        }
    }

    /**
     * Для обновления книги в консоли используем следующую запись:
     * <p>update 1 'Сказка о рыбаке без рыбки' 'А.С. Пушкин' 'Сказка'</p>
     * <p>или</p>
     * <p>update 1 'Сказка о рыбаке без рыбки'</p>
     */
    @ShellMethod("Обновить книгу")
    public String update(
            long id,
            String name,
            @ShellOption(defaultValue = "") String authorName,
            @ShellOption(defaultValue = "") String genreName) {
        try {
            return bookService.update(id, name, authorName, genreName).toString();
        } catch (Exception ex) {
            return ERROR_MESSAGE_PREFIX + ex.getMessage();
        }
    }

    /**
     * Для удаления книги в консоли используем следующую запись:
     * <p>delete 1</p>
     */
    @ShellMethod("Удалить книгу")
    public String delete(@ShellOption(defaultValue = "0") long id) {
        if (id == 0) {
            return ERROR_MESSAGE_PREFIX + "необходимо задать id книги";
        }
        try {
            bookService.delete(id);
            return DONE_MESSAGE;
        } catch (Exception ex) {
            return ERROR_MESSAGE_PREFIX + ex.getMessage();
        }
    }

    /**
     * Для создания комментария в консоли используем следующую запись (если book_id=1):
     * <p>create-comment 1 'title' 'good book!' 'user'</p>
     */
    @ShellMethod(value = "Создание комментария", key = "create-comment")
    public String createComment(long book, String title, String body, String user) {
        try {
            return String.valueOf(commentService.create(book, title, body, user));
        } catch (Exception ex) {
            return ERROR_MESSAGE_PREFIX + ex.getMessage();
        }
    }

    /**
     * Для удаления комментария в консоли используем следующую запись:
     * <p>del-comment 1</p>
     */
    @ShellMethod(value = "Удаление комментария", key = "del-comment")
    public String delComment(@ShellOption(defaultValue = "0") long id) {
        if (id == 0) {
            return ERROR_MESSAGE_PREFIX + "необходимо задать id комментария";
        }
        try {
            commentService.deleteById(id);
            return DONE_MESSAGE;
        } catch (Exception ex) {
            return ERROR_MESSAGE_PREFIX + ex.getMessage();
        }
    }
}
