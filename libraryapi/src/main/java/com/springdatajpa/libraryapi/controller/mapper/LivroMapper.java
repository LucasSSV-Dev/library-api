package com.springdatajpa.libraryapi.controller.mapper;

import com.springdatajpa.libraryapi.controller.dto.CadastroLivroDTO;
import com.springdatajpa.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import com.springdatajpa.libraryapi.model.Livro;
import com.springdatajpa.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "Spring", uses = {AutorMapper.class})
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;

    @Mapping(target = "autor", expression = "java( autorRepository.findById(dto.idAutor()).orElse(null) )")
    @Mapping(target = "data_publicacao", source = "dataPublicacao")
    public abstract Livro toEntity(CadastroLivroDTO dto);

    @Mapping(target = "dataPublicacao", source = "data_publicacao")
//    @Mapping(target = "autor", expression = "java( autorMapper.toAutorDTO(livro.getAutor()) )")
    public abstract ResultadoPesquisaLivroDTO toResultadoDTO(Livro livro);
}
