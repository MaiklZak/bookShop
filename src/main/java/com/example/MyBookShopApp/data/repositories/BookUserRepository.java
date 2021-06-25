package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.model.Book;
import com.example.MyBookShopApp.data.model.BookUser;
import com.example.MyBookShopApp.data.model.BookUserType;
import com.example.MyBookShopApp.security.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookUserRepository extends JpaRepository<BookUser, Integer> {

    BookUser findByBookAndUserAndType(Book book, BookstoreUser user, BookUserType type);
}
