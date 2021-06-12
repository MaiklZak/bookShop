package com.example.MyBookShopApp.data.reposirories;

import com.example.MyBookShopApp.data.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
