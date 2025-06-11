package com.springdatajpa.libraryapi.model;

import com.springdatajpa.libraryapi.controller.dto.AutorDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "autor")
@EntityListeners(AuditingEntityListener.class)// Sem isso o @CreatedDate e o @LastModifiedDate não conseguem operar e ele precisa do @EnableJpaAuditing lá no Application
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(length = 50, nullable = false)
    private String nacionalidade;

    @OneToMany(mappedBy = "id_autor", cascade = CascadeType.ALL)
    private List<Livro> livros = new ArrayList<>();

    @CreatedDate //Não preciso mais criar um LocalDateTime.now()
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @LastModifiedDate //Não preciso mais criar um LocalDateTime.now()
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "id_usuario")
    private UUID idUsuario;



    @Override
    public String toString() {
        return "Autor {\n" +
                "  id: " +
                id +
                ",\n" +
                "  nome: " +
                "'" + nome + "'" +
                ",\n" +
                "  data_nascimento: " +
                dataNascimento +
                ",\n" +
                "  nacionalidade: " +
                "'" + nacionalidade + "'" +
                "\n}";
    }

    public AutorDTO mapearParaAutorDTO(){
        return new AutorDTO(this.id, this.nome, this.dataNascimento, this.nacionalidade);
    }

    public void update(AutorDTO autor) {
        this.setNome(autor.nome());
        this.setDataNascimento(autor.dataNascimento());
        this.setNacionalidade(autor.nacionalidade());
    }
}
