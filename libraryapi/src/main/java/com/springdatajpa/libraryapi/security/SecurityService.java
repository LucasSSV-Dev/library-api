package com.springdatajpa.libraryapi.security;

import com.springdatajpa.libraryapi.model.Usuario;
import com.springdatajpa.libraryapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UsuarioService service;

    public Usuario obterUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails usuarioLogado = (UserDetails) authentication.getPrincipal();
        String login = usuarioLogado.getUsername();
        return service.obterPorLogin(login);
    }

}
