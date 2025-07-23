package com.springdatajpa.libraryapi.controller;

import com.springdatajpa.libraryapi.controller.dto.CadastroLivroDTO;
import com.springdatajpa.libraryapi.controller.dto.ErroResposta;
import com.springdatajpa.libraryapi.exceptions.AutorInexistenteException;
import com.springdatajpa.libraryapi.exceptions.RegistroDuplicadoException;
import com.springdatajpa.libraryapi.model.Autor;
import com.springdatajpa.libraryapi.model.GeneroLivro;
import com.springdatajpa.libraryapi.model.Livro;
import com.springdatajpa.libraryapi.service.AutorService;
import com.springdatajpa.libraryapi.service.LivroService;
import com.springdatajpa.libraryapi.validator.LivroValidador;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController {
    private final LivroService livroService;
    private final AutorService autorService;
    private final LivroValidador livroValidador;


    //Post:

    @PostMapping
    public ResponseEntity<Object> cadastrarLivro(@RequestBody @Valid CadastroLivroDTO cadastroLivroDTO){
        try {
            System.out.println("Entrada:");
            System.out.println(cadastroLivroDTO);

            Livro livro = cadastroLivroDTO.toLivro();
            livroValidador.validarLivro(livro);

            System.out.println("Livro criado pelo cadastroLivroDTO.toLivro():");
            System.out.println(livro);


            Optional<Autor> autorOptional = autorService.findById(cadastroLivroDTO.autorId());
            System.out.println("Checando se o id " + cadastroLivroDTO.autorId() + " existe no banco de dados.");

            if (autorOptional.isPresent()){
                System.out.println("Resultado:");
                System.out.println(autorOptional + "\n");

                Autor autor =  autorOptional.get();
                System.out.println("trazendo o optional pro autor:");
                System.out.println(autor + "\n");


                System.out.println("Livro antes de receber o autor:");
                System.out.println(livro);

                livro.setAutor(autor);
                System.out.println("Livro depois de receber o autor:");
                System.out.println(livro);


            } else{
                throw new AutorInexistenteException("Crie o autor antes de inserir o livro.");
            }

            livroService.cadastrarLivro(livro);


            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(livro.getId())
                    .toUri();

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
