package ar.utn.frba.ddsi.agregador.controllers;

import ar.utn.frba.ddsi.agregador.exceptions.HechoNoEncontradoException;
import ar.utn.frba.ddsi.agregador.exceptions.JustificacionInvalidaException;
import ar.utn.frba.ddsi.agregador.exceptions.UsuarioNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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


}
