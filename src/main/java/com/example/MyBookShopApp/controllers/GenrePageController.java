package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.GenreService;
import com.example.MyBookShopApp.data.dto.SearchWordDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class GenrePageController {


    private final GenreService genreService;

    public GenrePageController(GenreService genreService) {
        this.genreService = genreService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/genres")
    public String getGenrePage(Model model) {
        model.addAttribute("genresRoot", genreService.getGenresWithoutParent());
        model.addAttribute("genresDto", genreService.getListGenreDto());
        return "genres/index";
    }
}
