package com.example.demo.repository;

import com.example.demo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UserRepository extends JpaRepository<Users, Number> {
        Users findById(Long id);
}
