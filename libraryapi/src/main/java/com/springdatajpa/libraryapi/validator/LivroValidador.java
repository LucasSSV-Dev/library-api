package com.springdatajpa.libraryapi.validator;

import com.springdatajpa.libraryapi.exceptions.CampoInvalidoException;
import com.springdatajpa.libraryapi.exceptions.RegistroDuplicadoException;
import com.springdatajpa.libraryapi.model.Livro;
import com.springdatajpa.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidador {
    public final LivroRepository livroRepository;
    private static final int ANO_EXIGENCIA_PRECO = 2020;

    public void validarLivro(Livro livro) {
        if (existeLivroComIsbn(livro)) {
            throw new RegistroDuplicadoException("ISBN já cadastrado!");
        }
        if (isPrecoObrigatorioNulo(livro)){
            throw new CampoInvalidoException("preco", "Livros ");
        }
    }



    // True == invalidado - False = validado
    public boolean existeLivroComIsbn(Livro livro) {
        Optional<Livro> optionalLivro = livroRepository.findByIsbn(livro.getIsbn());

        if (livro.getId() == null) {
            return optionalLivro.isPresent();
        }else {
            return optionalLivro.map(value -> !livro.getId().equals(value.getId()))
                    .orElse(false);
        }
    }

    private boolean isPrecoObrigatorioNulo(Livro livro) {
        return livro.getPreco() == null && //Comparando com "2020" Então precisamos pegar só o ano da DataPublicacao.
                livro.getData_publicacao().getYear() >= ANO_EXIGENCIA_PRECO;
    }

}


