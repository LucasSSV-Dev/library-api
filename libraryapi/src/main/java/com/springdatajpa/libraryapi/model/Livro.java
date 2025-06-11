package com.springdatajpa.libraryapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "livro")
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
    @JoinColumn(name = "id_autor", nullable = false)
    private Autor id_autor; //Se o ID é de outra classe, você mostra aqui


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
                "  data_publicacao: " +
                data_publicacao +
                ",\n" +
                "  genero: " +
                "'" + genero + "'" +
                ",\n" +
                "  preco: " +
                preco +
                ",\n" +
                "  id_autor: " +
                id_autor +
                "\n}";
    }
}