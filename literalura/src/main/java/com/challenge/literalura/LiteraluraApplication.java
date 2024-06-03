package com.challenge.literalura;

import com.challenge.literalura.console.Main;
import com.challenge.literalura.repository.AuthorRepository;
import com.challenge.literalura.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private BookRepository book;
	private AuthorRepository author;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		Main consola = new Main(book,author);
		consola.startMenu();
	}
}
