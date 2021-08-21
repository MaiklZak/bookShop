package com.example.mybookshopapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class BooksControllerTests {

    private final MockMvc mockMvc;

    @Value("book-goo-056")
    String slug;

    @Value("sdfga233jsfs4r34253df")
    String hash;

    @Autowired
    BooksControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void bookPage() throws Exception {
        mockMvc.perform(get("/books/{slug}", slug))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/books/slug"))
                .andExpect(xpath("/html/body/div/div/main/div/div[1]/div[2]/div[1]/div[1]/a")
                        .string("Alistair Ector"));
    }

    @Test
    void saveNewBookImage() throws Exception {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file", "image.png", "img/png", "example picture".getBytes());
        mockMvc.perform(multipart("/books/{slug}/img/save/", slug).file(mockMultipartFile))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/" + slug));

    }

    @Test
    @Sql(value = "/scripts-test/test-data-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts-test/test-data-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("bogoke1616@eyeremind.com")
    void bookFile() throws Exception {
        mockMvc.perform(get("/books/download/{hash}", hash))
                .andDo(print())
                .andExpect(status().isOk());
    }
}