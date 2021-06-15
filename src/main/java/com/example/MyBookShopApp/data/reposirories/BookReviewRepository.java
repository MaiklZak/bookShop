package com.example.MyBookShopApp.data.reposirories;

import com.example.MyBookShopApp.data.model.BookReview;
import org.springframework.data.repository.CrudRepository;

public interface BookReviewRepository extends CrudRepository<BookReview, Integer> {
}