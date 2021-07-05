package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.security.entity.BookstoreUser;
import com.example.MyBookShopApp.security.repository.BookstoreUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

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
    public void testAddNewUser() {
        BookstoreUser user = new BookstoreUser();
        user.setPassword("123456");
        user.setPhone("1231231212");
        user.setName("Tester");
        user.setEmail("test@mail.org");

        assertNotNull(bookstoreUserRepository.save(user));
    }
}