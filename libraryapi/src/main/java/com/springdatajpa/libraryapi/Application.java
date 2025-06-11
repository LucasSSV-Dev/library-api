package com.springdatajpa.libraryapi;

import com.springdatajpa.libraryapi.model.Autor;
import com.springdatajpa.libraryapi.repository.AutorRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;

@SpringBootApplication
@EnableJpaAuditing //Sem isso aqui o @EntityListeners não funciona... Ele que permite a auditoria.
public class Application {

	public static void main(String[] args) {
		var context = SpringApplication.run(Application.class, args);
		AutorRepository repository = context.getBean(AutorRepository.class);

		//exemploSalvarRegistro(repository);
	}

	public static void exemploSalvarRegistro(AutorRepository autorRepository){
		Autor autor = new Autor();
		autor.setNome("José");
		autor.setNacionalidade("Brasileira");
		autor.setDataNascimento(LocalDate.of(1950, 1, 31));

		var autorSalvo = autorRepository.save(autor);
		System.out.println("Autor salvo: " + autorSalvo);
	}


}
