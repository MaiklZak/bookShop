package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

    @GetMapping("/documents")
    public String documentsPage() {
        return "info/documents";
    }
    @GetMapping("/about")
    public String aboutPage() {
        return "info/about";
    }
    @GetMapping("/faq")
    public String faqPage() {
        return "info/faq";
    }
    @GetMapping("/contacts")
    public String contactsPage() {
        return "info/contacts";
    }
}
