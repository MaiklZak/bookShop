package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.Book;
import com.example.MyBookShopApp.entity.BookUser;
import com.example.MyBookShopApp.entity.BookUserType;
import com.example.MyBookShopApp.security.entity.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookUserRepository extends JpaRepository<BookUser, Integer> {

    BookUser findByBookAndUserAndType(Book book, BookstoreUser user, BookUserType type);

    BookUser findByBook(Book book);

    BookUser findByBookAndUser(Book book, BookstoreUser user);
}
