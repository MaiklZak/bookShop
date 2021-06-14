package com.example.MyBookShopApp.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class BookShopCartControllerTests {

    private final MockMvc mockMvc;

    @Value("book-mqx-757/book-jjv-240/book-pnw-522")
    String cookieValue;

    @Value("book-jjv-240")
    String slugRemoveFromCart;

    @Value("book-psd-504")
    String slugAddInCart;

    @Autowired
    BookShopCartControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void handleCartRequest() throws Exception {
        mockMvc.perform(get("/books/cart").cookie(new Cookie("cartContents", cookieValue)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(xpath("/html/body/div/div/main/form/div/div/div[1]").nodeCount(3))
                .andExpect(xpath("//*[@id=\"3\"]")
                        .string("How to Get Ahead in Advertising"));
    }

    @Test
    void handleRemoveBookFromCartRequest() throws Exception {
        mockMvc.perform(post("/books/changeBookStatus/cart/remove/{slug}", slugRemoveFromCart)
                .cookie(new Cookie("cartContents", cookieValue)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/cart"))
                .andExpect(cookie().value("cartContents",
                        cookieValue.replace(slugRemoveFromCart, "").replaceAll("//", "/")));
    }

    @Test
    void handleChangeBookStatus() throws Exception {
        mockMvc.perform(post("/books/changeBookStatus/{slug}", slugAddInCart)
                .cookie(new Cookie("cartContents", cookieValue)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/" + slugAddInCart))
                .andExpect(cookie().value("cartContents", cookieValue + "/" + slugAddInCart));
    }
}