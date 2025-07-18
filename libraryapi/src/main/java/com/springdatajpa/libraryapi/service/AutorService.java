package com.springdatajpa.libraryapi.service;

import com.springdatajpa.libraryapi.controller.dto.AutorDTO;
import com.springdatajpa.libraryapi.exceptions.OperacaoNaoPermitidoException;
import com.springdatajpa.libraryapi.model.Autor;
import com.springdatajpa.libraryapi.repository.AutorRepository;
import com.springdatajpa.libraryapi.repository.LivroRepository;
import com.springdatajpa.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {
    private final AutorRepository autorRepository;
    private final AutorValidator validator;
    private final LivroRepository livroRepository;


    //Post Methods
    public void save(Autor autor) {
        validator.validar(autor);
        autorRepository.save(autor);
    }


    //Get Methods
    public Optional<Autor> findById(UUID id) {
        //É um Optional que retorna o Autor como AutorDTO caso ele seja encontrado. Se não ele retorna o Optional
        return autorRepository.findById(id); //O repository entrega um Optional
    }


    public ResponseEntity<List<AutorDTO>> pesquisa(String nome, String nacionalidade) {
        if (nome != null && nacionalidade != null){
            return ResponseEntity.ok(mapearParaListDTO(autorRepository.findByNomeAndNacionalidadeContainingIgnoreCase(nome, nacionalidade)));
        }
        if (nome != null){
            return ResponseEntity.ok(mapearParaListDTO(autorRepository.findByNomeContainingIgnoreCase(nome)));
        }
        if (nacionalidade != null){
            return ResponseEntity.ok(mapearParaListDTO(autorRepository.findByNacionalidadeContainingIgnoreCase(nacionalidade)));
        }
        return ResponseEntity.ok(mapearParaListDTO(autorRepository.findAll()));
    }

    public List<AutorDTO> pesquisaByExample(String nome, String nacionalidade){
        var autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher exampleMatcher = ExampleMatcher
                .matching()
                .withIgnorePaths("id", "dataNascimento", "dataCadastro")
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        //Eu costumo mapear para LISTDTO antes de entregar.
        Example<Autor> autorExample = Example.of(autor, exampleMatcher);
        //AI tive que mudar o return aqui :D
        return mapearParaListDTO(autorRepository.findAll(autorExample));
    }



    //Delete Methods

    public void deleteAutor(Autor autor) {
        if (possuiAutor(autor)){
            throw new OperacaoNaoPermitidoException("Ação não permitida. Autor possui livros cadastrados.");
        }
        autorRepository.delete(autor);
    }


    //Patch Methods




    //Put Methods
    public void atualizarAutor(UUID id, AutorDTO autor) {
        Optional<Autor> autorOptional = findById(id);
        if (autorOptional.isPresent()) { //Checa se o Optional tem conteúdo
            var autorFound = autorOptional.get(); //Traz o conteúdo pra variável
            validator.validar(autorFound); //Valida o autor, obviamente
            autorFound.update(autor); //Esse .update() faz um set das informações pro autorFound
            autorRepository.save(autorFound); //Como o AutorFound já possui um id, ele salva no lugar do anterior
        }
    }





    //Reusabilidade ativada!
    public List<AutorDTO> mapearParaListDTO(List<Autor> autorList){
        return autorList.stream().map(Autor::mapearParaAutorDTO).toList();
    }

    public boolean possuiAutor(Autor autor){
        return livroRepository.existsByAutor(autor);
    }
}
