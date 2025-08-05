package com.springdatajpa.libraryapi.controller.dto;

import java.util.List;

public record PaginaDeAutoresDTO(
        List<AutorDTO> dados,
        PaginacaoDTO paginacao
) {
}
