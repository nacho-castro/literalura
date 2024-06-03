package com.challenge.literalura.repository;

import com.challenge.literalura.model.Author;
import com.challenge.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author,Long> {
    Author findByNombreContainsIgnoreCase(String nombre);
}
