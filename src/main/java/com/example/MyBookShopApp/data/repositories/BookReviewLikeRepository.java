package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.model.BookReviewLike;
import org.springframework.data.repository.CrudRepository;

public interface BookReviewLikeRepository extends CrudRepository<BookReviewLike, Integer> {

    void deleteById(String s);
}
