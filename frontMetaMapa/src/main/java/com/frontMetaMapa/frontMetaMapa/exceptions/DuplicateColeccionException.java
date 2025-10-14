package com.frontMetaMapa.frontMetaMapa.exceptions;

public class DuplicateColeccionException extends RuntimeException {

    public DuplicateColeccionException(String id) {
        super("El id " + id + " ya existe");
    }
}
