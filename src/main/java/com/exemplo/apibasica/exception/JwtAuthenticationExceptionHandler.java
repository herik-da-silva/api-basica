package com.exemplo.apibasica.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class JwtAuthenticationExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtException.class)
    public Map<String, String> handleExpiredJwtException(ExpiredJwtException ex) {
        return Map.of("status", "error",
                "message", "Token expirado. Por favor, faça login novamente."
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException.class)
    public Map<String, String> handleJwtException(JwtException ex) {
        return Map.of("status", "error",
                "message", "Erro de autenticação. Token inválido."
        );
    }
}
