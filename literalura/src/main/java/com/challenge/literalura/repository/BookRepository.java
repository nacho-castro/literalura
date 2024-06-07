package com.challenge.literalura.repository;

import com.challenge.literalura.model.Book;
import com.challenge.literalura.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    @Query("SELECT b FROM Book b JOIN b.autor a WHERE b.titulo LIKE %:titulo%")
    Optional<Book> findBookByTitle(@Param("titulo") String titulo);

    @Query("SELECT b FROM Book b WHERE b.idioma = :l")
    List<Book> searchByLanguage(Language l);
}
