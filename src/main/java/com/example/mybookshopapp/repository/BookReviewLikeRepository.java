package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.BookReview;
import com.example.mybookshopapp.entity.BookReviewLike;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import org.springframework.data.repository.CrudRepository;

public interface BookReviewLikeRepository extends CrudRepository<BookReviewLike, Integer> {

    BookReviewLike findByBookReviewAndUser(BookReview bookReview, BookstoreUser user);
}
