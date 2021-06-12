package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class AuthorServiceTests {

    private final AuthorService authorService;

    @Autowired
    AuthorServiceTests(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Test
    void getAuthorsMap() {
        Map<String, List<Author>> authorsMap = authorService.getAuthorsMap();
        assertNotNull(authorsMap);
        assertFalse(authorsMap.isEmpty());
        for (Map.Entry<String, List<Author>> entry : authorsMap.entrySet()) {
            for (Author author : entry.getValue()) {
                assertThat(author.getLastName()).startsWith(entry.getKey());
            }
        }
    }
}