package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.model.BookReview;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookReviewRepository extends CrudRepository<BookReview, Integer> {
    List<BookReview> findAllByBookSlug(String slug);
}
