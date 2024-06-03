package com.challenge.literalura.console;

import com.challenge.literalura.model.Author;
import com.challenge.literalura.model.Book;
import com.challenge.literalura.model.DataBook;
import com.challenge.literalura.model.DataLibrary;
import com.challenge.literalura.repository.AuthorRepository;
import com.challenge.literalura.repository.BookRepository;
import com.challenge.literalura.service.ConnectApi;
import com.challenge.literalura.service.ConvertData;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private Scanner in = new Scanner(System.in);

    private ConnectApi connection = new ConnectApi();

    private ConvertData convert = new ConvertData();

    private final String URL_BASE = "https://gutendex.com/books/?search=";

    private BookRepository bookRepo;
    private AuthorRepository authorRepo;

    public Main(BookRepository bookRepo,AuthorRepository authorRepo){
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }

    public void startMenu(){
        var option = -1;
        do{
            System.out.println("\nElija una opcion:" +
                    "\n1. Buscar Libro por titulo" +
                    "\n2. Listar libros registrados" +
                    "\n3. Listar autores registrados" +
                    "\n4. Listar libros por idioma" +
                    "\n0. Salir");
            option = in.nextInt();
            in.nextLine();

            switch (option){
                case 1:
                    buscarLibroWeb();
                    break;
                default:
                    System.out.println("Opcion invalida!");
                    break;
            }
        }while(option != 0);
        System.out.println("Hasta luego!");
    }

    public void buscarLibroWeb(){
        //Busqueda de libro por titulo
        System.out.print("Ingrese titulo buscado: ");
        var tituloBuscado = in.nextLine();

        try {
            String encodeParam = URLEncoder.encode(tituloBuscado, "UTF-8");

            //Obtenemos un json del HTTP
            String json = connection.getDataWeb(URL_BASE + encodeParam);
            //Convertir Json a Objeto DataBook
            var datosApi = convert.getData(json, DataLibrary.class);

            //Filtrar libro buscado de la lista obtenida
            Optional<DataBook> libroBuscado = datosApi.libros().stream()
                    .filter(l-> l.titulo().equalsIgnoreCase(tituloBuscado))
                    .findFirst();

            if(libroBuscado.isPresent()){
                Book libroNuevo = new Book(libroBuscado.get());
                Author autor = authorRepo.findByNombreContainsIgnoreCase(libroNuevo.getAutor().getNombre());

                if (autor == null) {
                    // Si el autor no existe, créalo y asígnalo al libro
                    Author nuevoAutor = libroNuevo.getAutor();

                    //me retorna el autor con el id, entonces si paso el autor al libro con el id, al guardarlo no me duplicara el autor!!!!
                    autor = authorRepo.save(nuevoAutor);
                }

                try {
                    libroNuevo.setAutor(autor);
                    bookRepo.save(libroNuevo);
                    System.out.println(libroNuevo);
                } catch (DataIntegrityViolationException ex) {
                    // Manejar la excepción de restricción única
                    System.out.println("El libro con este título ya existe en la base de datos.");
                }

            } else {
                System.out.println("Libro no encontrado!");
            }

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
