package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.Book;
import com.example.mybookshopapp.entity.BookUser;
import com.example.mybookshopapp.entity.BookUserType;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookUserRepository extends JpaRepository<BookUser, Integer> {

    BookUser findByBookAndUserAndType(Book book, BookstoreUser user, BookUserType type);

    BookUser findByBook(Book book);

    BookUser findByBookAndUser(Book book, BookstoreUser user);
}
