package com.springdatajpa.libraryapi.controller.dto;


import com.springdatajpa.libraryapi.model.Autor;

import java.time.LocalDate;
import java.util.UUID;


public record AutorDTO(UUID id, String nome, LocalDate dataNascimento, String nacionalidade) {

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
