package com.example.demo.services.implementation;


import com.example.demo.rabbitconfiguration.RabbitConfig;
import com.example.demo.model.Books;
import com.example.demo.repository.BookRepository;
import com.example.demo.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@Service
@CacheConfig(cacheNames = "Books")
public class BookServiceImpl implements BookService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    @Override
    public List<Books> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Cacheable(key="#id")
    public Books findByBookId(Long id) {
        Books book=bookRepository.findBooksByBookId(id);
        return book;
    }


    @Override
    public Books saveBooks(Books book) {
        Books newBook = bookRepository.save(book);
        redisTemplate.opsForValue().set("book:" + newBook.getBookId(), newBook);
        System.out.println("User created: " + newBook.toString());

        return newBook;
    }


    @Override
    @CachePut(key="#id")
    public Books updateBooks(Long id, Books book) {
        Books updated_book=bookRepository.findBooksByBookId(id);
        if (updated_book != null) {
        updated_book.setBookName(book.getBookName());
        bookRepository.save(updated_book);
        System.out.println("book created: " + updated_book.toString());

        return updated_book;

        }
        return null;
    }


    @Override
    @CachePut(key = "#id")
    public Books addStock(Long id, int quantity) {
        Books book = bookRepository.findBooksByBookId(id);
        if (book != null) {
            int previousStock = book.getStock();
            book.setStock(previousStock + quantity);
            bookRepository.save(book);

            if (previousStock == 0 && book.getStock() > 0) {
                String message = "Stock for book ID " + id + " has been updated from 0 to " + book.getStock();
                System.out.println("book stock update: " + book.toString());
                logger.info("Sent message to RabbitMQ: {}", message);
            }
        }
        return book;
    }


    @Override
    @CacheEvict(key="#id",allEntries = true)
    public void deleteBooks(Long id) {
        Books delete_book=bookRepository.findBooksByBookId(id);

        if(delete_book!=null) {
            bookRepository.delete(delete_book);
            redisTemplate.delete("book:" + id);
            logger.info("Deleted book with ID: {}", id);
         } else {
            logger.warn("No book found with ID: {}", id);
        }
    }


}
