package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
