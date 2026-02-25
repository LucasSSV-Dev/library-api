package com.springdatajpa.libraryapi.security;

import com.springdatajpa.libraryapi.model.Usuario;
import com.springdatajpa.libraryapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Pegamos os dados que a autenticação enviou pra verificar se existe mesmo
        String login = authentication.getName();

        //Verificamos se o login existe no banco de dados
        Usuario usuarioEncontrado = usuarioService.obterPorLogin(login);
        if (usuarioEncontrado == null){
            throw getErroUsuarioNaoEncontrado(); //Se não existe, rola erro, obviamente.
        }

        //Se existir, vamos verificar a senha
        String senhaDigitada = authentication.getCredentials().toString();
        String senhaCriptografada = usuarioEncontrado.getSenha();

        //Se a senha bater é true, senão é false.
        boolean senhasBatem = passwordEncoder.matches(senhaDigitada, senhaCriptografada);
        if (senhasBatem){ //Se for false, ele ignora o que está no if.
            return new CustomAuthentication(usuarioEncontrado);
        }

        //Se ele ignorar o que está no if, ele bate aqui e envia erro pq a senha tá errada.
        throw getErroUsuarioNaoEncontrado();
    }

    private UsernameNotFoundException getErroUsuarioNaoEncontrado(){
        return new UsernameNotFoundException("Usuario e/ou senha incorretos!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
