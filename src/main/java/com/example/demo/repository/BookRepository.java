package com.example.demo.repository;

import com.example.demo.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Books,Number> {
  Books findBooksByBookId(Long id);
}
