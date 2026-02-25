package com.springdatajpa.libraryapi.exceptions;

public class OperacaoNaoPermitidoException extends RuntimeException {
    public OperacaoNaoPermitidoException(String message) {
        super(message);
    }

}
