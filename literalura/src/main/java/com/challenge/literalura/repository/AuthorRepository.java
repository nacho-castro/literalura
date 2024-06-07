package com.challenge.literalura.repository;

import com.challenge.literalura.model.Author;
import com.challenge.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> {

    @Query("SELECT a FROM Book b JOIN b.autor a WHERE a.nombre LIKE %:nombre%")
    Optional<Author> findAuthorByName(@Param("nombre") String nombre);
}
