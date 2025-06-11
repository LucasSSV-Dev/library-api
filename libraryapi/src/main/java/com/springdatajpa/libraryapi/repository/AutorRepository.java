package com.springdatajpa.libraryapi.repository;

import com.springdatajpa.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AutorRepository extends JpaRepository<Autor, UUID> {


    List<Autor> findByNomeAndNacionalidadeContainingIgnoreCase(String nome, String nacionalidade);

    List<Autor> findByNomeContainingIgnoreCase(String nome);

    List<Autor> findByNacionalidadeContainingIgnoreCase(String nacionalidade);

}
