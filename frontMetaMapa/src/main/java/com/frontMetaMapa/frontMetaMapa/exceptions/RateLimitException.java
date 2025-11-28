package com.frontMetaMapa.frontMetaMapa.exceptions;

public class RateLimitException extends RuntimeException {
    private final long segundos;

    public RateLimitException(long segundos) {
        super("Rate limit exceeded");
        this.segundos = segundos;
    }

    public long getSegundos() { return segundos; }
}