package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AuthorsController {

    private final AuthorService authorService;

    @Autowired
    public AuthorsController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ModelAttribute("authorsMap")
    public Map<String, List<Author>> authorsMap() {
        Map<String, List<Author>> authorsMap = authorService.getAuthorsMap();
        Map<String, List<String>> stringListMap = new HashMap<>();
        stringListMap.put("N", Arrays.asList("asd", "adad", "adad"));
        stringListMap.put("D", Arrays.asList("asd", "adad", "adad"));
        stringListMap.put("A", Arrays.asList("asd", "adad", "adad"));
        stringListMap.put("L", Arrays.asList("asd", "adad", "adad"));
        stringListMap.put("B", Arrays.asList("asd", "adad", "adad"));
        stringListMap.put("I", Arrays.asList("asd", "adad", "adad"));
        System.out.println(stringListMap);
        return authorsMap;
    }

    @GetMapping("/authors")
    public String authorsPage() {
        return "/authors/index";
    }
}
