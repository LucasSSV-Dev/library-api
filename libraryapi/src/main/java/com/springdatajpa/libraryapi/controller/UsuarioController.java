package com.springdatajpa.libraryapi.controller;

import com.springdatajpa.libraryapi.controller.dto.UsuarioDTO;
import com.springdatajpa.libraryapi.controller.mapper.UsuarioMapper;
import com.springdatajpa.libraryapi.model.Usuario;
import com.springdatajpa.libraryapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController{

    private final UsuarioService service;
    private final UsuarioMapper mapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvarUsuario(@RequestBody UsuarioDTO dto){
        Usuario usuario = mapper.toEntity(dto);
        service.salvar(usuario);
    }

    @GetMapping("/{login}")
    public UsuarioDTO obterPorLogin(@PathVariable String login){
        return mapper.toDTO(service.obterPorLogin(login));
    }


}
