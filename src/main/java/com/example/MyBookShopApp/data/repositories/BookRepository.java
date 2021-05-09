package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Override
    @EntityGraph(value = "Book.authorsAndGenres", type = EntityGraph.EntityGraphType.FETCH)
    List<Book> findAll();

    @EntityGraph(value = "Book.authorsAndGenres", type = EntityGraph.EntityGraphType.FETCH)
    List<Book> findBooksByAuthor_FirstName(String name);

//    @Query("FROM Book b JOIN Author a ON b.author.id = a.id")
    @EntityGraph(value = "Book.authorsAndGenres", type = EntityGraph.EntityGraphType.FETCH)
    @Query("FROM Book")
    List<Book> customFindAllBooks();

    @EntityGraph(value = "Book.authorsAndGenres", type = EntityGraph.EntityGraphType.FETCH)
    List<Book> findBookByUsers_Name(@Param("name") String name);
}
