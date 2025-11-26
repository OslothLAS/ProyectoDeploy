package com.frontMetaMapa.frontMetaMapa.exceptions;

public class RateLimitException extends RuntimeException {
    private final long segundosEspera;

    public RateLimitException(String message, long segundosEspera) {
        super(message);
        this.segundosEspera = segundosEspera;
    }

    public long getSegundosEspera() {
        return segundosEspera;
    }
}