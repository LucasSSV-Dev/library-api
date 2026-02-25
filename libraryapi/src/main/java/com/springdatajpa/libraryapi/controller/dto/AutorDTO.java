package com.springdatajpa.libraryapi.controller.dto;


import com.springdatajpa.libraryapi.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;


public record AutorDTO(UUID id,

                       @NotBlank(message = "Campo Obrigatório!")
                       @Size(min = 2, max = 100, message = "Limite de caracteres ultrapassado. Max: 100.")
                       String nome,

                       @NotNull(message = "Campo Obrigatório!")
                       @Past(message = "Não pode ser uma data futura.")
                       LocalDate dataNascimento,

                       @NotBlank(message = "Campo Obrigatório!")
                       @Size(min = 5, max = 20, message = "Limite de caracteres ultrapassado. Max: 50.")
                       String nacionalidade) {

    public Autor mapearParaAutor(){
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);
        return autor;
    }

    @Override
    public String toString() {
        return "AutorDTO {\n" +
                "  id: " +
                id +
                ",\n" +
                "  nome: " +
                "'" + nome + "'" +
                ",\n" +
                "  dataNascimento: " +
                dataNascimento +
                ",\n" +
                "  nacionalidade: " +
                "'" + nacionalidade + "'" +
                "\n}";
    }

}
