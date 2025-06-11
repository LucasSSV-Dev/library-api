package com.springdatajpa.libraryapi.controller.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErroResposta(int status, String mensagem, List<ErroCampo> erros) {

    public static ErroResposta respostaPadrao(String mensagem){
        return new ErroResposta(HttpStatus.BAD_REQUEST.value(), mensagem, List.of());
    }

    public static ErroResposta conflito(String mensagem){
        return new ErroResposta(HttpStatus.CONFLICT.value(), mensagem, List.of());
    }


    @Override
    public String toString() {
        return "ErroResposta {\n" +
                "  status: " +
                status +
                ",\n" +
                "  mensagem: " +
                "'" + mensagem + "'" +
                ",\n" +
                "  erros: " +
                erros +
                "\n}";
    }
}
