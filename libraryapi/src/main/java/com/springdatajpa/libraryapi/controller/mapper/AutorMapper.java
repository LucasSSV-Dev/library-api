package com.springdatajpa.libraryapi.controller.mapper;

import com.springdatajpa.libraryapi.controller.dto.AutorDTO;
import com.springdatajpa.libraryapi.model.Autor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    AutorDTO toAutorDTO(Autor autor);

    Autor toAutor(AutorDTO autorDTO);

}