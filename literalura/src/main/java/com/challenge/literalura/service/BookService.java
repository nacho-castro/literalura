package com.challenge.literalura.service;

import com.challenge.literalura.model.Book;
import com.challenge.literalura.model.Language;
import com.challenge.literalura.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepo;

    public void save(Book libro){
        bookRepo.save(libro);
    }

    public Optional<Book> findByTitle(String title){
        return bookRepo.findBookByTitle(title);
    }

    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    public List<Book> listByLanguage(String lang) {
        try {
            Language l = Language.fromString(lang);
            return bookRepo.searchByLanguage(l);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Idioma no válido.");
            return Collections.emptyList(); // Devuelve una lista vacía en caso de error
        }
    }
}
