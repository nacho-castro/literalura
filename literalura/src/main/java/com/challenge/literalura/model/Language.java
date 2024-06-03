package com.challenge.literalura.model;

public enum Language {
    SPANISH("es"),
    ENGLISH("en"),
    FRENCH("fr"),
    PORTUGUESE("pt");

    private String idioma;

    Language (String idioma){
        this.idioma = idioma;
    }

    public static Language fromString(String text) {
        for (Language lang : Language.values()) {
            if (lang.idioma.equalsIgnoreCase(text)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }

}
