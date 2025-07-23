package com.springdatajpa.libraryapi.exceptions;

public class AutorInexistenteException extends RuntimeException {
    public AutorInexistenteException(String message) {
        super(message);
    }
}
