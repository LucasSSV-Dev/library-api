package com.springdatajpa.libraryapi.repository;

import com.springdatajpa.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AutorRepository extends JpaRepository<Autor, UUID> {

    List<Autor> findByNomeAndNacionalidadeContainingIgnoreCase(String nome, String nacionalidade);

    List<Autor> findByNomeContainingIgnoreCase(String nome);

    List<Autor> findByNacionalidadeContainingIgnoreCase(String nacionalidade);



    //Ele fez com "find" e não com "exists" pq precisa chegar o Id pra atualizar.
    Optional<Autor> findByNomeAndDataNascimentoAndNacionalidade(String nome,
                                                          LocalDate dadaNascimento,
                                                          String nacionalidade);

}
