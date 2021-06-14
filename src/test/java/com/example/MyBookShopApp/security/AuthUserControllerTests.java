package com.example.MyBookShopApp.security;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
class AuthUserControllerTests {

    private final MockMvc mockMvc;

    @Autowired
    AuthUserControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void handleSignIn() throws Exception {
        mockMvc.perform(get("/signin"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signin"))
                .andExpect(xpath("/html/body/div/div[2]/main/form/div/div[1]/div[1]/label").exists());
    }

    @Test
    void handleSignUp() throws Exception {
        mockMvc.perform(get("/signup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(xpath("/html/body/div/div[1]/div/h1").exists());
    }

    @Test
    void handleRequestContactConfirmation() throws Exception {
        mockMvc.perform(post("/requestContactConfirmation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"contact\": \"+7 (123) 123-12-31\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"result\":\"true\"}"));

    }

    @Test
    void handleApproveContact() throws Exception {
        mockMvc.perform(post("/approveContact")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"contact\": \"+7 (111) 111-11-11\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"result\":\"true\"}"));
    }

    @Test
    void handleUserRegistration() throws Exception {
        mockMvc.perform(post("/reg")
                .contentType(APPLICATION_FORM_URLENCODED)
                .param("name", "Tester")
                .param("phone", "+7 (123) 123-12-31")
                .param("phoneCode", "123 123")
                .param("email", "tester@mail.com")
                .param("mailCode", "123 123")
                .param("pass", "123456"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signin"))
                .andExpect(xpath("/html/body/div/div[2]/main/form/div/div[1]/div[1]/label/span")
                        .string("Регистрация прошла успешно!"))
                .andExpect(model().attribute("regOK", true));
    }

    @Test
    @Sql(value = "/scripts-test/test-data-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts-test/test-data-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void handleLogin() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\": \"1234567\", \"contact\": \"yayota1045@naymio.com\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("token"));
    }

    @Test
    @Sql(value = "/scripts-test/test-data-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts-test/test-data-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("yayota1045@naymio.com")
    void handleMy() throws Exception {
        mockMvc.perform(get("/my"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("my"))
                .andExpect(xpath("/html/body/header/div[1]/div/div/div[3]/div/a[4]/span[1]")
                        .string("Дмитрий Петров"));
    }

    @Test
    void accessProfileFail() throws Exception {
        mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/signin"));
    }
}