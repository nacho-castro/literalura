package com.challenge.literalura.console;

import com.challenge.literalura.model.*;

import com.challenge.literalura.service.AuthorService;
import com.challenge.literalura.service.BookService;
import com.challenge.literalura.service.ConnectApi;
import com.challenge.literalura.service.ConvertData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Main {

    private Scanner in = new Scanner(System.in);
    private ConnectApi connection = new ConnectApi();
    private ConvertData convert = new ConvertData();
    private final String URL_BASE = "https://gutendex.com/books/?search=";

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    private List<Book> librosRegistrados;


    public void startMenu(){
        var option = -1;
        do{
            System.out.println("\nElija una opcion:" +
                    "\n1. Buscar Libro por titulo" +
                    "\n2. Listar libros registrados" +
                    "\n3. Listar autores registrados" +
                    "\n4. Listar libros por idioma" +
                    "\n5. Listar autores vivos" +
                    "\n6. Top 10 libros mas descargados" +
                    "\n7. Buscar Autor" +
                    "\n8. Estadisticas" +
                    "\n0. Salir");
            option = in.nextInt();
            in.nextLine();

            switch (option){
                case 1:
                    buscarLibroWeb();
                    break;
                case 2:
                    listBooks();
                    break;
                case 3:
                    listAuthors();
                    break;
                case 4:
                    listBooksByLanguage();
                    break;
                case 5:
                    authorsByDate();
                    break;
                case 6:
                    top10Books();
                    break;
                case 7:
                    findAuthor();
                    break;
                case 8:
                    statistics();
                    break;
                default:
                    System.out.println("Opcion invalida!");
                    break;
            }
        }while(option != 0);
        System.out.println("Hasta luego!");
    }

    public void buscarLibroWeb() {
        // Búsqueda de libro por título
        System.out.print("Ingrese título buscado: ");
        var tituloBuscado = in.nextLine();

        try {
            String encodeParam = URLEncoder.encode(tituloBuscado, "UTF-8");

            // Obtenemos un JSON del HTTP
            String json = connection.getDataWeb(URL_BASE + encodeParam);

            // Convertir JSON a clase DataLibrary (contiene TODOS los libros similares)
            var datosApi = convert.getData(json, DataLibrary.class);

            // Filtrar primer libro buscado de la lista obtenida
            Optional<DataBook> libroBuscado = datosApi.libros().stream()
                    .filter(l -> l.titulo().equalsIgnoreCase(tituloBuscado))
                    .findFirst();

            if (libroBuscado.isPresent()) {
                try {
                    // Convertir libro y autor de la API
                    List<Book> libroEncontrado = libroBuscado.stream().map(Book::new).collect(Collectors.toList());

                    Author autorAPI = libroBuscado.stream()
                            .flatMap(l -> l.autores().stream().map(Author::new))
                            .findFirst().orElse(null);

                    if (autorAPI == null) {
                        System.out.println("No se encontró el autor para el libro.");
                        return;
                    }

                    Optional<Author> autorBD = authorService.findByName(
                            libroBuscado.get().autores().stream()
                                    .map(DataAuthor::nombre)
                                    .collect(Collectors.joining())
                    );

                    Optional<Book> libroOptional = bookService.findByTitle(tituloBuscado);

                    if (libroOptional.isPresent()) {
                        System.out.println("El libro ya está guardado en la BD.");
                    } else {
                        Author autor;
                        if (autorBD.isPresent()) {
                            autor = autorBD.get();
                            System.out.println("El autor ya está guardado en la BD.");
                        } else {
                            autor = autorAPI;
                            authorService.save(autor);
                        }
                        // Asegurarse de que cada libro tenga el autor asignado y guardar el libro después del autor
                        for (Book libro : libroEncontrado) {
                            libro.setAutor(autor);
                            bookService.save(libro);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Warning! " + e.getMessage());
                }
            } else {
                System.out.println("Libro no encontrado!");
            }

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void listBooks(){
        //Obtener todos los libros de la BD
        librosRegistrados = bookService.getAllBooks();
        System.out.println("Total de libros: " + librosRegistrados.stream().count());
        librosRegistrados.stream().forEach(System.out::println);
    }

    public void listAuthors(){
        //Obtener todos los autores de la BD
        List<Author> autoresRegistrados = authorService.getAllAuthors();
        System.out.println("Total de autores: " + autoresRegistrados.stream().count());
        autoresRegistrados.stream().forEach(System.out::println);
    }

    public void listBooksByLanguage(){
        System.out.print("Español: es" + "\nIngles: en" + "\nFrances: fr" + "\nPortugues: pt\n" + "Ingrese idioma de la forma indicada (es,en,...): ");
        String lang = in.nextLine();

        try {
            librosRegistrados = bookService.listByLanguage(lang);
            System.out.println("Cant. de libros encontrados: " + librosRegistrados.stream().count());
            librosRegistrados.forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void authorsByDate(){
        System.out.print("Ingrese el año buscado: ");
        int year = in.nextInt();

        List<Author> autoresVivos = authorService.getAlive(year);
        System.out.println("Total de autores vivos en " + year + ": " + autoresVivos.stream().count());
        autoresVivos.forEach(System.out::println);
    }

    public void top10Books(){
        librosRegistrados = bookService.getAllBooks();
        librosRegistrados.stream()
                .sorted(Comparator.comparingInt(Book::getDescargas).reversed())
                .limit(10)
                .collect(Collectors.toList())
                .forEach(System.out::println);
    }

    public void findAuthor(){
        // Búsqueda de autor por nombre
        System.out.print("Ingrese nombre de autor buscado: ");
        var nombreBuscado = in.nextLine();

        Optional<Author> autorBuscado = authorService.findByName(nombreBuscado);

        if(autorBuscado.isPresent()){
            System.out.println("Se encontro: ");
            System.out.println(autorBuscado.get());
        } else {
            System.out.println("Autor no encontrado!");
        }
    }

    public void statistics(){
        //Estadisticas con funciones
        librosRegistrados = bookService.getAllBooks();

        DoubleSummaryStatistics est = librosRegistrados.stream()
                .filter(e -> e.getDescargas() > 0.0)
                .collect(Collectors.summarizingDouble(Book::getDescargas));
        System.out.println("\nRanking de descargas:");
        System.out.println("Media: " + String.format("%.2f", est.getAverage()) +
                "\nMas descargado: " + String.format("%.2f", est.getMax()) +
                "\nMenos descargado: " + String.format("%.2f", est.getMin()) + "\n");
    }
}
