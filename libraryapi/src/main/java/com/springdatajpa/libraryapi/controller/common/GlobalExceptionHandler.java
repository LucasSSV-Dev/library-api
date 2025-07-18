package com.springdatajpa.libraryapi.controller.common;


import com.springdatajpa.libraryapi.controller.dto.ErroCampo;
import com.springdatajpa.libraryapi.controller.dto.ErroResposta;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErroResposta handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
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





}
