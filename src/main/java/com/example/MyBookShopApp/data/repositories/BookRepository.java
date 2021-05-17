package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {


    List<Book> findBooksByAuthor_FirstName(String name);

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

    @Query(value = "" +
            "SELECT id, description, image, is_bestseller, discount, price, pub_date, slug, title, author_id " +
              "FROM books INNER JOIN " +
                   "(SELECT book_id, SUM(popul) AS popular " +
                      "FROM (SELECT book_id," +
                                   "type_id, " +
                                   "CASE " +
                                   "WHEN type_id = 3 THEN COUNT(*) " +
                                   "WHEN type_id = 2 THEN COUNT(*) * 0.7 " +
                                   "WHEN type_id = 1 THEN COUNT(*) * 0.4 " +
                                   "ELSE 0 END as popul " +
                              "FROM book2user bu INNER JOIN book2user_type but ON bu.type_id = but.id GROUP BY 1, 2) AS tab1 " +
                     "GROUP BY 1 ORDER BY 2 DESC) AS tab2 " +
                     "ON books.id = tab2.book_id " +
            "ORDER BY popular DESC", nativeQuery = true)
    List<Book> findPopularBooks(Pageable nextPage);

    List<Book> findBooksByPubDateAfterAndPubDateBefore(Date from, Date to, Pageable nextPage);

    List<Book> findBooksByPubDateBefore(Date to, Pageable nextPage);

    List<Book> findBooksByPubDateAfter(Date from, Pageable nextPage);

    @Query(value = "SELECT b.id, description, image, is_bestseller, discount, price, pub_date, slug, title, author_id " +
                     "FROM books b INNER JOIN book2tag bt ON b.id = bt.book_id " +
                                  "INNER JOIN tags t ON t.id = bt.tag_id " +
                    "WHERE t.id = :tagId", nativeQuery = true)
    Page<Book> findBooksByTagId(@Param(value = "tagId") Integer id, Pageable nextPage);

}
