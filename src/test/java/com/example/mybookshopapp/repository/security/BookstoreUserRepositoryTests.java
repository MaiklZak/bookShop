package com.example.mybookshopapp.repository.security;

import com.example.mybookshopapp.entity.security.BookstoreUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookstoreUserRepositoryTests {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    BookstoreUserRepositoryTests(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Test
    void testAddNewUser() {
        BookstoreUser user = new BookstoreUser();
        user.setHash(UUID.randomUUID().toString());
        user.setName("Tester");

        assertNotNull(bookstoreUserRepository.save(user));
    }
}