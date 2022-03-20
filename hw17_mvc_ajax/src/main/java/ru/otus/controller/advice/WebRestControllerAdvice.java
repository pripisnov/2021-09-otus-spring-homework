package ru.otus.controller.advice;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.dto.ErrorMessageDTO;
import ru.otus.exception.NotFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class WebRestControllerAdvice {
    private static final String ERROR_MESSAGE_PREFIX = "Ошибка при выполнении: ";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDTO> handleMethodArgumentException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(
                new ErrorMessageDTO(ERROR_MESSAGE_PREFIX + errorMessage),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorMessageDTO(ERROR_MESSAGE_PREFIX + ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDTO> handleCommonException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorMessageDTO(ERROR_MESSAGE_PREFIX + ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
