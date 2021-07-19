package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.Author;
import com.example.mybookshopapp.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {


    //    @Query(value = "SELECT * FROM authors a INNER JOIN books b ON a.id = b.author_id AND b = :books", nativeQuery = true)
    @Query("SELECT a FROM Author a, Book b WHERE a.id = b.author.id AND b IN :books")
    List<Author> findByBookIn(List<Book> books);
}
