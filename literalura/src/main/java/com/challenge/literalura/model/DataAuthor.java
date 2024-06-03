package com.challenge.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true) //Ignora campos no mapeados en esta clase
public record DataAuthor(
    @JsonAlias("name") String nombre,
    @JsonAlias("birth_year") Integer fechaNac,
    @JsonAlias("death_year") Integer fechaMuerte
) {
    @Override
    public String toString() {
        return "\nNombre:" + nombre +
                "\nFecha de nacimiento: " + fechaNac +
                "\nFecha de muerte: " + fechaMuerte + "\n";
    }
}
