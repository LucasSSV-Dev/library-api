package com.springdatajpa.libraryapi.service;

import com.springdatajpa.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import com.springdatajpa.libraryapi.model.Autor;
import com.springdatajpa.libraryapi.model.GeneroLivro;
import com.springdatajpa.libraryapi.model.Livro;
import com.springdatajpa.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository livroRepository;


    public void cadastrarLivro(Livro livro) {
        livroRepository.save(livro);
    }


    public List<Livro> findAll() {
        return livroRepository.findAll();
    }


    public List<Livro> pesquisaLivroByExample(String titulo, GeneroLivro genero, String isbn, String autorNome, LocalDate publicacao) {
        //Crio e insiro os valores do livro
        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setGenero(genero);
        livro.setIsbn(isbn);
        livro.setData_publicacao(publicacao);
        //Preciso criar um autor pra inserir em autor e ter pelo menos o nome xD No banco de dados não sei se vai ter.
        Autor autor = new Autor();
        autor.setNome(autorNome);
        livro.setAutor(autor);

        System.out.println("Resultado:");
        System.out.println(livro);

        ExampleMatcher exampleMatcher = ExampleMatcher
                .matching()
                .withIgnorePaths("id", "dataCadastro", "dataAtualizacao", "preco")
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Livro> livroExample = Example.of(livro, exampleMatcher);

        return livroRepository.findAll(livroExample);
    }

    public boolean deleteById(UUID id) {
        //Tento achar pra ver se tem
        Optional<Livro>  livro = livroRepository.findById(id);
        //Se tiver ele deleta e retorna true
        if (livro.isPresent()) {
            livroRepository.deleteById(id);
            return true;
        }
        //Se não, retorna false
        return false;
    }



    public List<ResultadoPesquisaLivroDTO> toResultadoDTOList(List<Livro> livros) {
        List<ResultadoPesquisaLivroDTO> output = new ArrayList<>();

        for (Livro livro : livros){
            output.add(new ResultadoPesquisaLivroDTO(livro.getId(), livro.getTitulo(), livro.getPreco(), livro.getIsbn(), livro.getData_publicacao(), livro.getGenero(), livro.getAutor().mapearParaAutorDTO()));
        }
        return output;
    }


}
