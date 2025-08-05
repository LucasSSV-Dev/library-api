package com.springdatajpa.libraryapi.controller;

import com.springdatajpa.libraryapi.controller.dto.CadastroLivroDTO;
import com.springdatajpa.libraryapi.controller.dto.ErroResposta;
import com.springdatajpa.libraryapi.controller.mapper.LivroMapper;
import com.springdatajpa.libraryapi.exceptions.RegistroDuplicadoException;
import com.springdatajpa.libraryapi.model.GeneroLivro;
import com.springdatajpa.libraryapi.model.Livro;
import com.springdatajpa.libraryapi.service.AutorService;
import com.springdatajpa.libraryapi.service.LivroService;
import com.springdatajpa.libraryapi.validator.LivroValidador;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController implements GenericController{
    private final LivroService livroService;
    private final AutorService autorService;
    private final LivroValidador livroValidador;
    private final LivroMapper mapper;


    //Post:

    @PostMapping
    public ResponseEntity<Object> cadastrarLivro(@RequestBody @Valid CadastroLivroDTO dto){
        try {
            Livro livro = mapper.toEntity(dto);
            livroService.cadastrarLivro(livro);

            URI location = gerarHeaderLocation(livro.getId());


            return ResponseEntity.created(location).build();

    } catch(RegistroDuplicadoException e){
        var erroDTO = ErroResposta.conflito(e.getMessage());
        return ResponseEntity.status(erroDTO.status()).body(erroDTO);
    }
    }


    //Get:

    @GetMapping
    public ResponseEntity<Object> PesquisaLivroByExample(
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "genero", required = false) GeneroLivro genero,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "autorNome", required = false)  String autorNome,
            @RequestParam(value = "publicacao", required = false) LocalDate publicacao
            ){

        var livro = livroService.toResultadoDTOList(
                livroService.pesquisaLivroByExample(titulo, genero, isbn, autorNome, publicacao)
        );
        
        return ResponseEntity.ok().body(livro);
    }


    //Delete

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id){
        //Tenta excluir. Se ele conseguir vai dar true. Se não vai dar false
        if (livroService.deleteById(id)){
            return ResponseEntity.noContent().build(); //Se excluiu vida que segue
        }
        return ResponseEntity.notFound().build(); //Se não excluiu, aparece não encontrado
    }



}
