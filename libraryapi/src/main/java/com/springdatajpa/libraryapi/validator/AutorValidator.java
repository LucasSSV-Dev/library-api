package com.springdatajpa.libraryapi.validator;


import com.springdatajpa.libraryapi.exceptions.RegistroDuplicadoException;
import com.springdatajpa.libraryapi.model.Autor;
import java.util.Optional;
import com.springdatajpa.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

@Component
public class AutorValidator {
    private final AutorRepository repository;

    public AutorValidator(AutorRepository repository) {
        this.repository = repository;
    }

    public void validar(Autor autor){
        if (existeAutorCadastrado(autor)){ //Se for true vai dar erro
            throw new RegistroDuplicadoException("Autor já cadastrado!");
        }
    }

    //Pra inicio de conversa, vai retornar um true ou um false. true vai gerar o erro, false vai deixar passar!
    private boolean existeAutorCadastrado(Autor autor){
        Optional<Autor> autorEncontrado = repository.findByNomeAndDataNascimentoAndNacionalidade(
                autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade()
        );
        //Se o autor tiver o id == null, é um novo autor!
        if (autor.getId() == null){
            //Sendo um novo autor, se espera que ele não exista, gerando um false e deixando passar :D
            return autorEncontrado.isPresent(); //Se existir ele vai informar que já existe
        }
        // Se o autor tiver um ID, se trata de uma atualização então precisamos checar se:
        // Existe algum autor.
        // Se o ID dele é igual ao do autor encontrado. Se ele existe(true), se é igual(!true) um true invertido pra false. Então da false!
        // No final das contas, queremos que dê um false se o autor existir e for o mesmo citado, pra que possamos fazer um PUT
        return autorEncontrado.isPresent() && !autorEncontrado.get().getId().equals(autor.getId());
    }
}
