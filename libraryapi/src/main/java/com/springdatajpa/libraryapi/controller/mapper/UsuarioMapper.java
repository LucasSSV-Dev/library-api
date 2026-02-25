package com.springdatajpa.libraryapi.controller.mapper;

import com.springdatajpa.libraryapi.controller.dto.UsuarioDTO;
import com.springdatajpa.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO usuarioDTO);

    UsuarioDTO toDTO(Usuario usuario);

}
