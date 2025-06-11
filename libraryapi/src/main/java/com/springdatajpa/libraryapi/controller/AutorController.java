package com.springdatajpa.libraryapi.controller;

import com.springdatajpa.libraryapi.controller.dto.AutorDTO;
import com.springdatajpa.libraryapi.model.Autor;
import com.springdatajpa.libraryapi.service.AutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("autores")
//http://localhost:8080/autores
public class AutorController {
    private final AutorService service;

    public AutorController(AutorService service) {
        this.service = service;
    }


    //Post Mappings
    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody AutorDTO autor){ //O ResponseEntity precisa de um parâmetro, nesse caso o Void
        var autorEntidade = autor.mapearParaAutor(); //Usando o conversor criado no DTO para devolver um Autor :D
        service.save(autorEntidade); //Enviamos o autor pro Repository guardar no banco de Dados

        //A location tem o endereço http desse autor.
        //A location é tipo: http://localhost:8080/autores/{id}
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autorEntidade.getId())
                .toUri();


        return ResponseEntity.created(location).build();
    }



    //GET Mappings
    //todo: Acho que está correto. Perguntar ao GPT.
    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> findAutor(@PathVariable UUID id){
        Optional<Autor> autorDTOOptional = service.findById(id);
        //A IDE quer deixar mais simples com um Lambda, mas não gostei do jeito que ficou... ficou meio confuso pra mim
        //noinspection OptionalIsPresent
        if (autorDTOOptional.isPresent()){
            return ResponseEntity.ok(autorDTOOptional.get().mapearParaAutorDTO());
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisa(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade){

        return service.pesquisa(nome, nacionalidade);
    }



    //DELETE Mappings
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAutor(@PathVariable UUID id){
        return service.deleteAutor(id);

    }

    @DeleteMapping
    public ResponseEntity<Void> deletarTudo(){
        return service.deleteAll();
    }

    //PUT Mappings
    //todo: CONSERTAR!!!!!!!!!
    @PutMapping("{id}")
    public ResponseEntity<Void> atualizarAutor(@PathVariable UUID id,
                                               @RequestBody AutorDTO autor){
        return service.atualizarAutor(id, autor);



    }








}
