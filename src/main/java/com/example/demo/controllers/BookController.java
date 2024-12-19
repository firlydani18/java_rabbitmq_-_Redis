package com.example.demo.controllers;

import com.example.demo.rabbitproducer.NotificationProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.example.demo.rabbitconfiguration.RabbitConfig;
import com.example.demo.model.Books;
import com.example.demo.services.BookService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private NotificationProducer producer;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Books>> getAllBooks() {
        List<Books> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getBookById(@PathVariable Long id) {
        Books book = bookService.findByBookId(id);
        if (book == null) {
            // Mengembalikan 404 dengan pesan jika user tidak ditemukan
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("book with ID " + id + " not found.");
        }
        System.out.println("User found: " + book.toString());
        return new ResponseEntity<>("The book is\n " + book, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createBook(@RequestBody Books book) {
        Books savedBook = bookService.saveBooks(book);
        producer.sendMessage("books", "create", savedBook);
        return ResponseEntity.status(201).body(savedBook.toString());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBooks(@PathVariable Long id, @RequestBody Books book) {
        Books existingBook = bookService.findByBookId(id);
        if (existingBook != null) {
            existingBook.setBookName(book.getBookName());
            Books updatedBook = bookService.saveBooks(existingBook);
            producer.sendMessage("books", "update", updatedBook);
            return new ResponseEntity<>("The book with following id is updated with new values " + id + "\n" + updatedBook, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PutMapping("/{id}/stock")
//    public ResponseEntity<String> addStock(@PathVariable Long id, @RequestParam int quantity) {
//        Books updatedBook = bookService.addStock(id);
//        updatedBook.setStock(updatedBook.getStock() + quantity);
//        rabbitTemplate.convertAndSend(ProductRabbitConfiguration.PRODUCT_EXCHANGE, ProductRabbitConfiguration.PRODUCT_ROUTING_KEY, updatedBook);
//
//        return new ResponseEntity<>("The book with following id is updated with new values " + id + "\n" + updatedBook, HttpStatus.OK);
//    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<String> addStock(@PathVariable Long id, @RequestParam int quantity) {
        Books updatedBook = bookService.findByBookId(id);
        //Books updatedBook = bookService.addStock(id, quantity);

        if (updatedBook != null) {
            int currentStock = updatedBook.getStock();
            updatedBook.setStock(currentStock + quantity);
            bookService.saveBooks(updatedBook);
            producer.sendMessage("books", "update", updatedBook);
            return new ResponseEntity<>("The book with following id is updated with new stock value " + id + "\n" + updatedBook, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBooks(id);
        producer.sendMessage("books", "delete", id);
        return new ResponseEntity<>("The book with following ID has been deleted: " + id, HttpStatus.OK);
    }


}
