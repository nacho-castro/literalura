package com.challenge.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) //Ignora campos no mapeados en esta clase
public record DataBook(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DataAuthor> autores,
        @JsonAlias("languages") List<String> idioma,
        @JsonAlias("download_count") Integer descargas
) {
    @Override
    public String toString() {
        return "-----LIBRO-----" +
                "\nTitulo:" + titulo +
                "\nAutor: " + autores +
                "\nIdioma: " + idioma +
                "\nDescargas: " + descargas +
                "\n------------\n";
    }
}
