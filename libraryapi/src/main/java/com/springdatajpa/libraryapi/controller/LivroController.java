package com.springdatajpa.libraryapi.controller;

import com.springdatajpa.libraryapi.controller.dto.CadastroLivroDTO;
import com.springdatajpa.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import com.springdatajpa.libraryapi.controller.mapper.LivroMapper;
import com.springdatajpa.libraryapi.model.GeneroLivro;
import com.springdatajpa.libraryapi.model.Livro;
import com.springdatajpa.libraryapi.service.LivroService;
import com.springdatajpa.libraryapi.validator.LivroValidador;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController implements GenericController{
    private final LivroService livroService;
//    private final AutorService autorService; //Não estava usando...
    private final LivroValidador livroValidador;
    private final LivroMapper mapper;


    //Post:

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> cadastrarLivro(@RequestBody @Valid CadastroLivroDTO dto){
            Livro livro = mapper.toEntity(dto);
            livroValidador.validarLivro(livro);

            livroService.cadastrarLivro(livro);

            URI location = gerarHeaderLocation(livro.getId());

            return ResponseEntity.created(location).build();
    }


    //Get:

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> Pesquisa(
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "genero", required = false) GeneroLivro genero,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "autorNome", required = false)  String autorNome,
            @RequestParam(value = "publicacao", required = false) LocalDate publicacao
            ){

        List<ResultadoPesquisaLivroDTO> resultadoDTOList = livroService.toResultadoDTOList(
                livroService.pesquisaLivroByExample(titulo, genero, isbn, autorNome, publicacao)
        );
        
        return ResponseEntity.ok().body(resultadoDTOList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> PesquisaLivroById(@PathVariable UUID id){
        Optional<Livro> livroOptional = livroService.obterPorId(id);
        if (livroOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        ResultadoPesquisaLivroDTO livroDTO = mapper.toResultadoDTO(livroOptional.get());
        return ResponseEntity.ok().body(livroDTO);
    }


    //Delete

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> deleteById(@PathVariable UUID id){
        //Tenta excluir. Se ele conseguir vai dar true. Se não vai dar false
        if (livroService.deleteById(id)){
            return ResponseEntity.noContent().build(); //Se excluiu vida que segue
        }
        return ResponseEntity.notFound().build(); //Se não excluiu, aparece não encontrado
    }

    //Put

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> atualizar(@PathVariable UUID id, @RequestBody @Valid CadastroLivroDTO dto){
        return livroService.obterPorId(id).map(livro -> {
            Livro entidadeAux = mapper.toEntity(dto);

            livro.setIsbn(entidadeAux.getIsbn());
            livro.setTitulo(entidadeAux.getTitulo());
            livro.setData_publicacao(entidadeAux.getData_publicacao());
            livro.setGenero(entidadeAux.getGenero());
            livro.setPreco(entidadeAux.getPreco());
            livro.setAutor(entidadeAux.getAutor());

            livroValidador.validarLivro(livro);
            livroService.atualizarLivro(livro);

            return ResponseEntity.noContent().build();

            //Se não rolar, Not Found...
        }).orElse(ResponseEntity.notFound().build());
    }


}
