package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.Book;
import com.example.mybookshopapp.entity.BookUserType;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findBooksByAuthorFirstName(String name);

    @Query("FROM Book")
    List<Book> customFindAllBooks();

    //NEW BOOK REST REPOSITORY COMMANDS

    List<Book> findBooksByAuthorFirstNameContaining(String authorFirstName);

    List<Book> findBooksByTitleContaining(String bookTitle);

    List<Book> findBooksByPriceOldBetween(Integer min, Integer max);

    List<Book> findBooksByPriceOldIs(Integer price);

    @Query("FROM Book WHERE isBestseller = 1")
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM books WHERE discount = (SELECT MAX(discount) FROM books)", nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();

    Page<Book> findBookByTitleContaining(String bookTitle, Pageable nextPage);

    Book findBookBySlug(String slug);

    List<Book> findBooksBySlugIn(String[] slugs);

    @Query("SELECT b FROM Book b, BookUser bu WHERE b.id = bu.book.id AND bu.user = :user AND bu.type = :type")
    List<Book> findBooksByUserAndType(BookstoreUser user, BookUserType type);

    @Query("SELECT b FROM Book b, BookUser bu WHERE b.id = bu.book.id AND bu.user = :user")
    List<Book> findBooksByUser(BookstoreUser user);

    @Query(value = "SELECT id, description, image, is_bestseller, discount, price, pub_date, slug, title, author_id " +
            "FROM books INNER JOIN " +
            "(SELECT book_id, SUM(popul) AS popular " +
            "FROM (SELECT book_id, " +
            "bu.type, " +
            "CASE " +
            "WHEN bu.type = 'PAID' THEN COUNT(*) " +
            "WHEN bu.type = 'CART' THEN COUNT(*) * 0.7 " +
            "WHEN bu.type = 'KEPT' THEN COUNT(*) * 0.4 " +
            "ELSE 0 END as popul " +
            "FROM book2user bu GROUP BY 1, 2) AS tab1 " +
            "GROUP BY 1 ORDER BY 2 DESC) AS tab2 " +
            "ON books.id = tab2.book_id " +
            "ORDER BY popular DESC", nativeQuery = true)
    List<Book> findPopularBooks(Pageable nextPage);

    @Query(value = "SELECT b.id, description, image, is_bestseller, discount, price, pub_date, slug, title, author_id " +
            "FROM books b INNER JOIN book_tag bt ON b.id = bt.book_id " +
            "INNER JOIN tags t ON t.id = bt.tag_id " +
            "WHERE t.id = :tagId", nativeQuery = true)
    Page<Book> findBooksByTagId(@Param(value = "tagId") Integer id, Pageable nextPage);

}
