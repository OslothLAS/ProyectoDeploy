package com.usuarios.servicioDeUsuarios.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ValidationException extends RuntimeException {
    private final Map<String, String> fieldErrors = new HashMap<>();

    public ValidationException(String message) {
        super(message);
    }

    public void addFieldError(String field, String error) {
        fieldErrors.put(field, error);
    }

    public boolean hasFieldErrors() {
        return !fieldErrors.isEmpty();
    }
}
