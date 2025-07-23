package com.springdatajpa.libraryapi.controller.dto;

import com.springdatajpa.libraryapi.model.GeneroLivro;
import com.springdatajpa.libraryapi.model.Livro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ResultadoPesquisaLivroDTO(
        UUID id,
        String titulo,
        BigDecimal preco,
        String isbn,
        LocalDate dataPublicacao,
        GeneroLivro genero,
        AutorDTO autor
) {
}
