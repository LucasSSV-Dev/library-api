package com.springdatajpa.libraryapi.service;

import com.springdatajpa.libraryapi.controller.dto.AutorDTO;
import com.springdatajpa.libraryapi.model.Autor;
import com.springdatajpa.libraryapi.repository.AutorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {
    private final AutorRepository repository;

    public AutorService(AutorRepository autorRepository) {
        this.repository = autorRepository;
    }

    //Post Methods
    public void save(Autor autor) {
        repository.save(autor);
    }


    //Get Methods
    public Optional<Autor> findById(UUID id) {
        //É um Optional que retorna o Autor como AutorDTO caso ele seja encontrado. Se não ele retorna o Optional
        return repository.findById(id); //O repository entrega um Optional
    }


    public ResponseEntity<List<AutorDTO>> pesquisa(String nome, String nacionalidade) {
        if (nome != null && nacionalidade != null){
            return ResponseEntity.ok(mapearParaListDTO(repository.findByNomeAndNacionalidadeContainingIgnoreCase(nome, nacionalidade)));
        }
        if (nome != null){
            return ResponseEntity.ok(mapearParaListDTO(repository.findByNomeContainingIgnoreCase(nome)));
        }
        if (nacionalidade != null){
            return ResponseEntity.ok(mapearParaListDTO(repository.findByNacionalidadeContainingIgnoreCase(nacionalidade)));
        }
        return ResponseEntity.ok(mapearParaListDTO(repository.findAll()));
    }


    //Delete Methods

    public ResponseEntity<Void> deleteAutor(UUID id) {
        Optional<Autor> autorOptional = repository.findById(id);
        if (autorOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    public ResponseEntity<Void> deleteAll() {
        repository.deleteAll();
        return ResponseEntity.noContent().build();
    }


    //Patch Methods




    //Put Methods
    public ResponseEntity<Void> atualizarAutor(UUID id, AutorDTO autor) {
        Optional<Autor> autorOptional = findById(id);
        if (autorOptional.isPresent()) { //Checa se o Optional tem conteúdo
            var autorFound = autorOptional.get(); //Traz o conteúdo pra variável
            autorFound.update(autor); //Esse .update() faz um set das informações pro autorFound
            repository.save(autorFound); //Como o AutorFound já possui um
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }





    //Reusabilidade ativada!

    public List<AutorDTO> mapearParaListDTO(List<Autor> autorList){
        return autorList.stream().map(Autor::mapearParaAutorDTO).toList();
    }
}
