package com.springdatajpa.libraryapi.controller.common;


import com.springdatajpa.libraryapi.controller.dto.ErroCampo;
import com.springdatajpa.libraryapi.controller.dto.ErroResposta;
import com.springdatajpa.libraryapi.exceptions.CampoInvalidoException;
import com.springdatajpa.libraryapi.exceptions.OperacaoNaoPermitidoException;
import com.springdatajpa.libraryapi.exceptions.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


// Aqui vai ser tratado td erro que acontecer no resto do código! Ele puxa através do @ExceptionHandler.
// O que estiver dentro do @ExceptionHandler(), vai ser lidado da forma como ele dita.
@RestControllerAdvice
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErroResposta handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        //Selecionando apenas o que precisamos da Exception
        List<FieldError> fieldErrors = exception.getFieldErrors();
        // Transformando cada erro em um ErroCampo e criando uma lista deles.
        List<ErroCampo> erroCampoList = fieldErrors.stream()
                .map(fieldError -> new ErroCampo(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        //Entregando tudo bonitinho no erro resposta :D
        return new ErroResposta(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de Validação.",
                erroCampoList
        );
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta handleRegistroDuplicadoException(RegistroDuplicadoException exception) {
        return ErroResposta.conflito(exception.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitidoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleOperacaoNaoPermitidoException(OperacaoNaoPermitidoException exception) {
        return ErroResposta.respostaPadrao(exception.getMessage());
    }

    @ExceptionHandler(CampoInvalidoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleCampoInvalidoException(CampoInvalidoException exception) {
        return new ErroResposta(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação.",
                List.of(new ErroCampo(exception.getCampo(), exception.getMessage())));
    }
//todo: Acha uma forma de usar esses exception pra alguma coisa.
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErroResposta handleAccessDeniedException(AccessDeniedException exception) {
        return new ErroResposta(HttpStatus.FORBIDDEN.value(), "Acesso negado!", List.of());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleErrosNaoTratados(RuntimeException exception) {
        return new ErroResposta(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro Inesperado. Entre em contato com nosso suporte.",
                List.of()
        );
    }
}
