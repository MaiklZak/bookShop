package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.dto.GenreDto;
import com.example.MyBookShopApp.data.model.Genre;
import com.example.MyBookShopApp.data.repositories.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreDto> getListGenreDto() {
        return genreRepository.findAll().stream()
                .filter(genre -> genre.getParentId() != null)
                .map(genre -> new GenreDto(genre,
                        genreRepository.findGenresByParentId(genre.getId()),
                        genreRepository.getOne(genre.getParentId())))
                .sorted((o1, o2) -> o2.getCountBooks() - o1.getCountBooks())
                .collect(Collectors.toList());
    }

    public List<Genre> getGenresWithoutParent() {
        List<Genre> genres = genreRepository.findGenresByParentId(null);
        genres.sort((o1, o2) -> o2.getBooks().size() - o1.getBooks().size());
        return genres;
    }
}
