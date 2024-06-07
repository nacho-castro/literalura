package com.challenge.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Author autor;

    @Enumerated(EnumType.STRING)
    private Language idioma;

    private Integer descargas;

    public Book(){

    }

    public Book(DataBook libroDatos){
        this.titulo = libroDatos.titulo();
        this.idioma = Language.fromString(libroDatos.idioma().get(0));
        this.autor = new Author(libroDatos.autores().get(0));
        this.descargas = libroDatos.descargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Author getAutor() {
        return autor;
    }

    public void setAutor(Author autor) {
        this.autor = autor;
    }

    public Language getIdioma() {
        return idioma;
    }

    public void setIdioma(Language idioma) {
        this.idioma = idioma;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return "-----LIBRO-----" +
                "\nTitulo:" + titulo +
                "\nAutor: " + autor.getNombre() +
                "\nIdioma: " + idioma +
                "\nDescargas: " + descargas +
                "\n------------------\n";
    }
}
