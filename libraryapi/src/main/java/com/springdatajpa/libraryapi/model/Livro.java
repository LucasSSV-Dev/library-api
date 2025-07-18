package com.springdatajpa.libraryapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "livro")
@EntityListeners(AuditingEntityListener.class) //Lembre disso!!!!
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Column(name = "data_publicacao", nullable = false)
    private LocalDate data_publicacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", length = 30, nullable = false)
    private GeneroLivro genero; //EnumType precisa ser da Enum solicitada

    @Column(name = "preco", precision = 18, scale = 2)
    private BigDecimal preco;

    @ManyToOne //(cascade = CascadeType.ALL)
    @JoinColumn(name = "livros", nullable = false)
    private Autor autor; //Se o ID é de outra classe, você mostra aqui

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
        return "LivroEntity {\n" +
                "  id: " +
                id +
                ",\n" +
                "  isbn: " +
                "'" + isbn + "'" +
                ",\n" +
                "  titulo: " +
                "'" + titulo + "'" +
                ",\n" +
                "  dataPublicacao: " +
                data_publicacao +
                ",\n" +
                "  genero: " +
                "'" + genero + "'" +
                ",\n" +
                "  preco: " +
                preco +
                ",\n" +
                "  id_autor: " +
                autor +
                "\n}";
    }
}