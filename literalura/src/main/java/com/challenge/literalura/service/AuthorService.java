package com.challenge.literalura.service;

import com.challenge.literalura.model.Author;
import com.challenge.literalura.model.Book;
import com.challenge.literalura.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepo;

    public void save(Author author){
        authorRepo.save(author);
    }

    public Optional<Author> findByName(String name){
        return authorRepo.findAuthorByName(name);
    }

    public List<Author> getAllAuthors() {
        return authorRepo.findAll();
    }

    public List<Author> getAlive(int year){
        return authorRepo.findAlive(year);
    }
}
