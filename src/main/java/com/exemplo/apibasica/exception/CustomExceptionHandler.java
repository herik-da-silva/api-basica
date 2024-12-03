package com.exemplo.apibasica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Trata exceções de recursos não encontrados eargumentos inválidos
 * Exe: IllegalArgumentException
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND) // Retorna 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public Map<String, String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return Map.of(
                "status", "error",
                "message", ex.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // Retorna 400 para argumentos inválidos
    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return Map.of(
                "status", "error",
                "message", ex.getMessage()
        );
    }

}
