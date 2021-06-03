package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.model.BookRating;
import org.springframework.data.repository.CrudRepository;

public interface BookRatingRepository extends CrudRepository<BookRating, Integer> {
}
