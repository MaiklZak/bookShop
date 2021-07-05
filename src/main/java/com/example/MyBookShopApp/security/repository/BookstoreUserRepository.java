package com.example.MyBookShopApp.security.repository;

import com.example.MyBookShopApp.security.entity.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookstoreUserRepository extends JpaRepository<BookstoreUser, Integer> {

    BookstoreUser findBookstoreUserByEmail(String email);

    BookstoreUser findBookstoreUserByPhone(String phone);

    BookstoreUser findBookstoreUserByHash(String userHash);
}
