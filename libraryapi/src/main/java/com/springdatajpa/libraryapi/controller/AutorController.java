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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO dto){ //O ResponseEntity precisa de um parâmetro, nesse caso o Void
        //Mapper pra criar a entidade Autor
        Autor autor = mapper.toAutor(dto); //Usando o Mapper para devolver um Autor :D
        service.save(autor); //Enviamos o autor pro Service preparar e enviar pro Repository.

        //A location tem o endereço http desse autor.
        //A location é tipo: http://localhost:8080/autores/{id}
        URI location = gerarHeaderLocation(autor.getId());

        //Resposta bonitinha pros clientes. com o endereço do autor no site.
        return ResponseEntity.created(location).build();
    }



    //GET Mappings
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'OPERADOR')")
    public ResponseEntity<AutorDTO> findAutor(@PathVariable UUID id){
        return service.findById(id)
                .map(mapper::toAutorDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'OPERADOR')")
    public ResponseEntity<Object> pesquisa(
            @RequestParam(value = "nome", required = false) String nome,

            @RequestParam(value = "nacionalidade", required = false) String nacionalidade,

            @PageableDefault(size = 3, sort = "nome") Pageable pageable){

        var resultado = service.pesquisaByPage(nome, nacionalidade, pageable);

        return ResponseEntity.ok().body(resultado);
    }


    //DELETE Mappings
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
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
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Object> atualizarAutor(@PathVariable UUID id,
                                               @RequestBody @Valid AutorDTO autor){

        service.atualizarAutor(id, autor);

        return ResponseEntity.noContent().build();
    }






}
