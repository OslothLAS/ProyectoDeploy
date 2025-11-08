package com.usuarios.servicioDeUsuarios.controllers;
import com.usuarios.servicioDeUsuarios.exceptions.AuthException;
import com.usuarios.servicioDeUsuarios.exceptions.UsuarioYaExisteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ex.getReason());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseException(HttpMessageNotReadableException ex) {

        String mensajeError = "El JSON enviado tiene un formato incorrecto o está malformado.";

        Throwable causaRaiz = ex.getCause();
        while (causaRaiz != null && causaRaiz.getCause() != null) {
            causaRaiz = causaRaiz.getCause();
        }

        if (causaRaiz instanceof DateTimeParseException) {
            mensajeError = "El formato de fecha es incorrecto. Por favor, use el formato estándar YYYY-MM-DD.";
        }

        // Usa un Map en lugar de ErrorResponse
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensajeError);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioYaExiste(UsuarioYaExisteException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, String>> handleAuthException(AuthException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errores = ex.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(
                        error -> ((FieldError) error).getField(),
                        error -> error.getDefaultMessage()
                ));
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "La solicitud tiene campos inválidos.");
        response.put("detalles", errores);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
