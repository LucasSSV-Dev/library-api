package com.springdatajpa.libraryapi.exceptions;

import com.springdatajpa.libraryapi.controller.dto.ErroResposta;
import org.springframework.http.HttpStatus;

import java.util.List;

public class OperacaoNaoPermitidoException extends RuntimeException {
    public OperacaoNaoPermitidoException(String message) {
        super(message);
    }

}
