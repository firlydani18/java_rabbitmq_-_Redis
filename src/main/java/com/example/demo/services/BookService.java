package com.example.demo.services;

import com.example.demo.model.Books;

import java.util.List;

public interface BookService {

    List<Books> getAllBooks();
    Books findByBookId(Long id);
    Books saveBooks(Books book);
    Books updateBooks(Long id, Books book);
    Books addStock(Long id, int quantity);
    void deleteBooks(Long id);
}
