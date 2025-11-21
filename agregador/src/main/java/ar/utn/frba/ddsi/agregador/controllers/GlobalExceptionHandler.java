package ar.utn.frba.ddsi.agregador.controllers;

import ar.utn.frba.ddsi.agregador.exceptions.HechoNoEncontradoException;
import ar.utn.frba.ddsi.agregador.exceptions.JustificacionInvalidaException;
import ar.utn.frba.ddsi.agregador.exceptions.SolicitudNoEncontradaException;
import ar.utn.frba.ddsi.agregador.exceptions.UsuarioNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JustificacionInvalidaException.class)
    public ResponseEntity<String> handleJustificacionInvalida(JustificacionInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<String> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // o NOT_FOUND si preferís 404
                .body(ex.getMessage());
    }

    @ExceptionHandler(HechoNoEncontradoException.class)
    public ResponseEntity<String> handleHechoNoEncontrado(HechoNoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ex.getReason());
    }

    @ExceptionHandler(SolicitudNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleSolicitudNoEncontrada(SolicitudNoEncontradaException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND); // <-- 404
    }

}
