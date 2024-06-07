package com.challenge.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    private Integer fechaNac;
    private Integer fechaMuerte;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> libros = new ArrayList<>();

    public Author(){
    }

    public Author(DataAuthor autor){
        this.nombre = autor.nombre();
        this.fechaNac = autor.fechaNac();
        this.fechaMuerte = autor.fechaMuerte();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Integer fechaNac) {
        this.fechaNac = fechaNac;
    }

    public Integer getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(Integer fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public List<Book> getLibros() {
        return libros;
    }

    public void setLibros(List<Book> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return "\nNombre: " + nombre +
                "\nFecha de nacimiento: " + fechaNac +
                "\nFecha de muerte: " + fechaMuerte +
                "\nLibros: " + this.libros.stream().map(Book::getTitulo).collect(Collectors.toList());
    }
}
