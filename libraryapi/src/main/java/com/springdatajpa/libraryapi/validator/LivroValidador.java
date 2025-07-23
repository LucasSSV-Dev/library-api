package com.springdatajpa.libraryapi.validator;

import com.springdatajpa.libraryapi.exceptions.RegistroDuplicadoException;
import com.springdatajpa.libraryapi.model.Livro;
import com.springdatajpa.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LivroValidador {
    public final LivroRepository livroRepository;

    public void validarLivro(Livro livro) {

        if (livro.getId() == null && livroRepository.existsByIsbn(livro.getIsbn())) {
            throw new RegistroDuplicadoException("ISBN já Cadastrado!");
        }

    }

}


