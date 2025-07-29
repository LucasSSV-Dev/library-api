package com.springdatajpa.libraryapi.controller.mapper;

import com.springdatajpa.libraryapi.controller.dto.AutorDTO;
import com.springdatajpa.libraryapi.model.Autor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    AutorDTO ToAutorDTO(Autor autor);

    Autor ToAutor(AutorDTO autorDTO);

}
