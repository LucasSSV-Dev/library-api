package com.springdatajpa.libraryapi.service;

import com.springdatajpa.libraryapi.model.Livro;
import com.springdatajpa.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository livroRepository;


    public void cadastrarLivro(Livro livro) {
        livroRepository.save(livro);
    }



}
