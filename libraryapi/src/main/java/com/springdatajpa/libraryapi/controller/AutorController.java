package com.springdatajpa.libraryapi.controller;

import com.springdatajpa.libraryapi.controller.dto.AutorDTO;
import com.springdatajpa.libraryapi.controller.mapper.AutorMapper;
import com.springdatajpa.libraryapi.model.Autor;
import com.springdatajpa.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("autores")
//http://localhost:8080/autores
public class AutorController implements GenericController{
    private final AutorService service;
    private final AutorMapper  mapper;


    //Post Mappings
    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO dto){ //O ResponseEntity precisa de um parâmetro, nesse caso o Void
            Autor autor = mapper.toAutor(dto); //Usando o conversor criado no DTO para devolver um Autor :D
            service.save(autor); //Enviamos o autor pro Repository guardar no banco de Dados

            //A location tem o endereço http desse autor.
            //A location é tipo: http://localhost:8080/autores/{id}
            URI location = gerarHeaderLocation(autor.getId());

            return ResponseEntity.created(location).build();
    }



    //GET Mappings
    //todo: Acho que está correto. Perguntar ao GPT.
    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> findAutor(@PathVariable UUID id){
        return service.findById(id).map(Autor -> {
            AutorDTO dto = mapper.toAutorDTO(Autor);
            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }


    @GetMapping
    public ResponseEntity<Object> pesquisa(
            @RequestParam(value = "nome", required = false) String nome,

            @RequestParam(value = "nacionalidade", required = false) String nacionalidade,

            @PageableDefault(page = 0, size = 3, sort = "nome") Pageable pageable){

        var resultado = service.pesquisaByPage(nome, nacionalidade, pageable);

        return ResponseEntity.ok().body(resultado);
    }


    //DELETE Mappings
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteAutor(@PathVariable UUID id){
            Optional<Autor> autorOptional = service.findById(id);

            //Se não existir esse autor, não encontrado!
            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            //Dentro do service tem uma validação checando se tem livro desse autor cadastrado
            service.deleteAutor(autorOptional.get()); //Se tiver livro vai retornar um erro e pulamos pro "Catch"

            //Se não tem livro cadastrado, vamos deletar e entregar um "noContent" (Sucesso sem conteúdo).
            return ResponseEntity.noContent().build();
    }


    //PUT Mappings
    @PutMapping("{id}")
    public ResponseEntity<Object> atualizarAutor(@PathVariable UUID id,
                                               @RequestBody @Valid AutorDTO autor){

        service.atualizarAutor(id, autor);

        return ResponseEntity.noContent().build();
    }






}
