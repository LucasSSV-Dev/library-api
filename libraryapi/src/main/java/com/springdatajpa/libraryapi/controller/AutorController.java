package com.springdatajpa.libraryapi.controller;

import com.springdatajpa.libraryapi.controller.dto.AutorDTO;
import com.springdatajpa.libraryapi.controller.dto.ErroResposta;
import com.springdatajpa.libraryapi.exceptions.OperacaoNaoPermitidoException;
import com.springdatajpa.libraryapi.exceptions.RegistroDuplicadoException;
import com.springdatajpa.libraryapi.model.Autor;
import com.springdatajpa.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("autores")
//http://localhost:8080/autores
public class AutorController {
    private final AutorService service;


    //Post Mappings
    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO autor){ //O ResponseEntity precisa de um parâmetro, nesse caso o Void
        try {
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

        } catch(RegistroDuplicadoException e){
            var erroDTO = ErroResposta.conflito(e.getMessage());
          return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
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
    public ResponseEntity<Object> deleteAutor(@PathVariable UUID id){
        try {
            Optional<Autor> autorOptional = service.findById(id);

            //Se não existir esse autor, não encontrado!
            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            //Dentro do service tem uma validação checando se tem livro desse autor cadastrado
            service.deleteAutor(autorOptional.get()); //Se tiver livro vai retornar um erro e pulamos pro "Catch"

            //Se não tem livro cadastrado, vamos deletar e entregar um "noContent" (Sucesso sem conteúdo).
            return ResponseEntity.noContent().build();

        } catch (OperacaoNaoPermitidoException e){
            ErroResposta erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }


    //PUT Mappings
    @PutMapping("{id}")
    public ResponseEntity<Object> atualizarAutor(@PathVariable UUID id,
                                               @RequestBody AutorDTO autor){
        try{
            service.atualizarAutor(id, autor);

        } catch (RegistroDuplicadoException e){

            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);

        }
        return ResponseEntity.noContent().build();
    }






}
