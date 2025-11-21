package com.usuarios.servicioDeUsuarios.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String entidad, String id) {
        super("No se ha encontrado " + entidad + " de id " + id);
    }
}
