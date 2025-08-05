package com.springdatajpa.libraryapi.controller.dto;

public record PaginacaoDTO(
        int paginaAtual,
        int tamanhoPagina,
        int totalPaginas,
        long totalElementos
) {
}
