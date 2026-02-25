package com.springdatajpa.libraryapi.security;

import com.springdatajpa.libraryapi.model.Usuario;
import com.springdatajpa.libraryapi.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoginSocialSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UsuarioService service;
    private static final String SENHA_PADRAO = "googleAuth";

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        //Transforma a authentication no token do Google
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        //Pega apenas os dados do usuário que vieram do Google
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
        //Resgata o email
        String email = oAuth2User.getAttribute("email");
        //busca o email no banco de dados
        Usuario usuarioEncontrado = service.obterPorEmail(email);
        //se não encontrar um usuario vamos cadastrar
        if (usuarioEncontrado == null) {
            cadastrarUsuarioNaBase(email);
            usuarioEncontrado = service.obterPorEmail(email); //Agora vai encontrar o usuario :D
        }
        authentication = new CustomAuthentication(usuarioEncontrado);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        super.onAuthenticationSuccess(request, response, authentication);

    }

    private void cadastrarUsuarioNaBase(String email) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(obterLoginApartirDoEmail(email));
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(SENHA_PADRAO);
        novoUsuario.setRoles(List.of("OPERADOR"));

        service.salvar(novoUsuario);
    }

    private String obterLoginApartirDoEmail(String email) {
        return email.substring(0, email.indexOf("@"));
    }
}
