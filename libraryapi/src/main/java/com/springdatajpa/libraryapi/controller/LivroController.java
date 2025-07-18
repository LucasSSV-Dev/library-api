package com.springdatajpa.libraryapi.controller;

import com.springdatajpa.libraryapi.controller.dto.ErroResposta;
import com.springdatajpa.libraryapi.controller.dto.CadastroLivroDTO;
import com.springdatajpa.libraryapi.exceptions.RegistroDuplicadoException;
import com.springdatajpa.libraryapi.model.Autor;
import com.springdatajpa.libraryapi.model.Livro;
import com.springdatajpa.libraryapi.service.AutorService;
import com.springdatajpa.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController {
    private final LivroService livroService;
    private final AutorService autorService;


    @PostMapping
    public ResponseEntity<Object> cadastrarLivro(@RequestBody @Valid CadastroLivroDTO cadastroLivroDTO){
        try {
            Livro livro = cadastroLivroDTO.toLivro();

            Optional<Autor> autorOptional = autorService.findById(cadastroLivroDTO.autorId());
            if (autorOptional.isPresent()){
                livro.setAutor(autorOptional.get());
            } else {
                livro.setAutor(null);
            }
            livroService.cadastrarLivro(livro);


            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(livro.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
    } catch(
    RegistroDuplicadoException e){
        var erroDTO = ErroResposta.conflito(e.getMessage());
        return ResponseEntity.status(erroDTO.status()).body(erroDTO);
    }
    }




}
