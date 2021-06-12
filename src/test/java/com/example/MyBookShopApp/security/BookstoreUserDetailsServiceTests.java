package com.example.MyBookShopApp.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookstoreUserDetailsServiceTests {

    private BookstoreUser user;

    private final BookstoreUserDetailsService bookstoreUserDetailsService;

    @MockBean
    BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    BookstoreUserDetailsServiceTests(BookstoreUserDetailsService bookstoreUserDetailsService) {
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
    }

    @BeforeEach
    void setUp() {
        user = new BookstoreUser();
        user.setId(1);
        user.setName("TestUser");
        user.setEmail("mail@mail.org");
        user.setPassword("123456");
        user.setPhone("9879878787");
    }

    @Test
    void loadUserByUsername() {
        Mockito.doReturn(user)
                .when(bookstoreUserRepository)
                .findBookstoreUserByEmail(user.getEmail());
        BookstoreUserDetails userByEmail = (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(user.getEmail());
        assertNotNull(userByEmail);
        assertThat(userByEmail.getBookstoreUser()).isEqualTo(user);
    }
}