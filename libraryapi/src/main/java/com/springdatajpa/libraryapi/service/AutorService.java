package com.springdatajpa.libraryapi.service;

import com.springdatajpa.libraryapi.controller.dto.AutorDTO;
import com.springdatajpa.libraryapi.controller.dto.PaginaDeAutoresDTO;
import com.springdatajpa.libraryapi.controller.dto.PaginacaoDTO;
import com.springdatajpa.libraryapi.exceptions.OperacaoNaoPermitidoException;
import com.springdatajpa.libraryapi.model.Autor;
import com.springdatajpa.libraryapi.repository.AutorRepository;
import com.springdatajpa.libraryapi.repository.LivroRepository;
import com.springdatajpa.libraryapi.security.SecurityService;
import com.springdatajpa.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final SecurityService securityService;


    //Post Methods
    public void save(Autor autor) {
        validator.validar(autor);
        autor.setUsuario(securityService.obterUsuarioLogado());
        autorRepository.save(autor);
    }


    //Get Methods
    public Optional<Autor> findById(UUID id) {
        //É um Optional que retorna o Autor como AutorDTO caso ele seja encontrado. Se não ele retorna o Optional
        return autorRepository.findById(id); //O repository entrega um Optional
    }


//    public List<AutorDTO> pesquisa(String nome, String nacionalidade) {
//        if (nome != null && nacionalidade != null){
//            return mapearParaListDTO(autorRepository.findByNomeAndNacionalidadeContainingIgnoreCase(nome, nacionalidade));
//        }
//        if (nome != null){
//            return mapearParaListDTO(autorRepository.findByNomeContainingIgnoreCase(nome));
//        }
//        if (nacionalidade != null){
//            return mapearParaListDTO(autorRepository.findByNacionalidadeContainingIgnoreCase(nacionalidade));
//        }
//        return mapearParaListDTO(autorRepository.findAll());
//    }


//    public List<AutorDTO> pesquisaByExample(String nome, String nacionalidade){
//        var autor = new Autor();
//        autor.setNome(nome);
//        autor.setNacionalidade(nacionalidade);
//
//        ExampleMatcher exampleMatcher = ExampleMatcher
//                .matching()
//                .withIgnorePaths("id", "dataNascimento", "dataCadastro")
//                .withIgnoreNullValues()
//                .withIgnoreCase()
//                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
//
//        //Eu costumo mapear para LISTDTO antes de entregar.
//        Example<Autor> autorExample = Example.of(autor, exampleMatcher);
//        //AI tive que mudar o return aqui :D
//        return mapearParaListDTO(autorRepository.findAll(autorExample));
//    }

    public PaginaDeAutoresDTO pesquisaByPage(String nome, String nacionalidade, Pageable pageable){
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

        //Suposto resultado. Cru demais! Ai vou facilitar
        Page<Autor> paginaAutores = autorRepository.findAll(autorExample, pageable);

        // getContent pra pegar só o conteúdo da "Page<Autor>"
        List<AutorDTO> dados = paginaAutores
                .getContent()
                .stream()
                .map(Autor::mapearParaAutorDTO)
                .toList();

        PaginacaoDTO paginacao = new PaginacaoDTO(
                paginaAutores.getNumber() + 1, // naturalmente, o indice ZERO é a PRIMEIRA página.
                paginaAutores.getSize(),       // tamanho da página
                paginaAutores.getTotalPages(),
                paginaAutores.getTotalElements()
        );

        return new PaginaDeAutoresDTO(dados, paginacao);
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
            autorRepository.save(autorFound); //Como o AutorFound já possui um ‘id’, ele salva no lugar do anterior
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
