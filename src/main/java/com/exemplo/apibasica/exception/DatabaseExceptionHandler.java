package com.exemplo.apibasica.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class DatabaseExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Map<String, String> handleDatabaseExceptions(DataIntegrityViolationException ex) {
        return Map.of(
                "status", "error",
                "message", "Operação no banco de dados falhou. Verifique os dados e tente novamente."
        );
    }
}
