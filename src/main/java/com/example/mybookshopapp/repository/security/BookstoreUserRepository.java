package com.example.mybookshopapp.repository.security;

import com.example.mybookshopapp.entity.security.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookstoreUserRepository extends JpaRepository<BookstoreUser, Integer> {

    BookstoreUser findBookstoreUserByEmail(String email);

    BookstoreUser findBookstoreUserByPhone(String phone);

    BookstoreUser findBookstoreUserByHash(String userHash);
}
