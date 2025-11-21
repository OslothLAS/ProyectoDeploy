package ar.utn.ba.ddsi.fuenteDinamica.controllers;

import ar.utn.ba.ddsi.fuenteDinamica.exceptions.HechoNoEncontradoException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HechoNoEncontradoException.class)
    public ResponseEntity<String> handleHechoNoEncontrado(HechoNoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, String>> handleExpiredJwtException(ExpiredJwtException ex) {

        Map<String, String> error = new HashMap<>();

        error.put("mensaje", "Tu token de sesión ha expirado. Por favor, vuelve a iniciar sesión.");

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED); // <-- 401
    }
}

