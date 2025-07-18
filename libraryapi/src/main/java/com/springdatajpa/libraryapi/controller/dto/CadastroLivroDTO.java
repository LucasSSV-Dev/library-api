package com.springdatajpa.libraryapi.controller.dto;

import com.springdatajpa.libraryapi.model.GeneroLivro;
import com.springdatajpa.libraryapi.model.Livro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


public record CadastroLivroDTO(
        @NotBlank(message = "Campo Obrigatório!")
        @Size(min = 3, max = 150, message = "Limite de caracteres ultrapassado. Max: 100.")
        String titulo,
        BigDecimal preco,
        @NotBlank(message = "Campo Obrigatório!")
        @Size(max = 20, message = "Limite de caracteres ultrapassado. Max: 100.")
        String isbn,
        @NotNull(message = "Campo Obrigatório!")
        LocalDate dataPublicacao,
        GeneroLivro genero,
        @NotNull(message = "Campo Obrigatório!")
        UUID autorId) {



    public Livro toLivro(){
        Livro livro = new Livro();
        livro.setTitulo(this.titulo);
        livro.setPreco(this.preco);
        livro.setIsbn(this.isbn);
        livro.setData_publicacao(this.dataPublicacao);
        livro.setGenero(this.genero);

        return livro;
    }

    @Override
    public String toString() {
        return "LivroDTO {\n" +
                "  titulo: " +
                "'" + titulo + "'" +
                ",\n" +
                "  preco: " +
                preco +
                ",\n" +
                "  isbn: " +
                "'" + isbn + "'" +
                ",\n" +
                "  dataPublicacao: " +
                dataPublicacao +
                ",\n" +
                "  genero: " +
                genero +
                ",\n" +
                "  autor: " +
                autorId +
                "\n}";
    }
}
