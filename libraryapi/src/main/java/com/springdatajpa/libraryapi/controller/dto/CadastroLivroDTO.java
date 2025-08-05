package com.springdatajpa.libraryapi.controller.dto;

import com.springdatajpa.libraryapi.model.GeneroLivro;
import com.springdatajpa.libraryapi.model.Livro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


public record CadastroLivroDTO(
        @ISBN(type = ISBN.Type.ANY, message = "ISBN inválido")
        @NotBlank(message = "Campo Obrigatório!")
        String isbn,

        @NotBlank(message = "Campo Obrigatório!")
        @Size(min = 3, max = 150, message = "Limite de caracteres ultrapassado. Max: 100.")
        String titulo,

        @NotNull(message = "Campo Obrigatório!")
        @Past(message = "Não pode ser uma data futura")
        LocalDate dataPublicacao,

        @NotNull(message = "Campo Obrigatório!")
        UUID idAutor,

        //Não é obrigatório
        GeneroLivro genero,

        //Não é obrigatório
        BigDecimal preco

        ) {


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
                idAutor +
                "\n}";
    }
}
