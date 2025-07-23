package com.springdatajpa.libraryapi.repository;

import com.springdatajpa.libraryapi.model.Autor;
import com.springdatajpa.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LivroRepository extends JpaRepository<Livro, UUID > {
    boolean existsByAutor(Autor Autor);

    boolean existsByIsbn(String isbn);
}
