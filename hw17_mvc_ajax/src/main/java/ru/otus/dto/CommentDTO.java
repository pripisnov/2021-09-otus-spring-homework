package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private long id;
    private long bookId;
    @NotEmpty(message = "Текст титула для комментария не должен быть пустым")
    @Size(min = 5, message = "Длина титула для комментария не может быть меньше 5 символов")
    private String title;
    @NotEmpty(message = "Текст комментария не должен быть пустым")
    @Size(min = 10, message = "Длина комментария не может быть меньше 10 символов")
    private String body;
    @NotBlank(message = "Имя пользователя не должно быть пустым")
    @Size(min = 1, max = 40, message = "Имя пользователя должно быть длиной между 1 и 40 символами")
    private String userName;
}
