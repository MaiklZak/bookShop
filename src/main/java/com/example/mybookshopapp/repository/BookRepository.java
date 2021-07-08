package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.*;
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

    @Query(value = "SELECT b.id, description, image, is_bestseller, discount, price, pub_date, slug, title, author_id " +
            "        FROM books b " +
            "             INNER JOIN book2tag bt ON b.id = bt.book_id " +
            "             INNER JOIN tags t ON t.id = bt.tag_id " +
            "       WHERE t.id = :tagId", nativeQuery = true)
    Page<Book> findBooksByTagId(@Param(value = "tagId") Integer id, Pageable nextPage);

    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.id = :id")
    List<Book> findBooksByGenreId(@Param("id") Integer id, Pageable nextPage);

    @Query(value = "SELECT * FROM books" +
            "                INNER JOIN " +
            "                      (SELECT b.id book_id, ROUND(AVG(br.value)) rating " +
            "                         FROM books b INNER JOIN book_rating br ON b.id = br.book_id " +
            "                       GROUP BY b.id) as tab_1 ON books.id = tab_1.book_id " +
            "        ORDER BY rating DESC, books.pub_date DESC", nativeQuery = true)
    List<Book> findRecommendedBooksSortRatingAndRecent(Pageable nextPage);

    @Query("SELECT b FROM Book b, BookUser bu WHERE b.id = bu.book.id AND bu.user = :user AND bu.type IN :types")
    List<Book> findBooksByUserAndTypeIn(BookstoreUser user, BookUserType[] types);

    @Query(value =
            "SELECT * FROM books INNER JOIN " +
            "       (SELECT b.id bid, COUNT(*) n FROM books b " +
            "               INNER JOIN authors a     ON b.author_id = a.id " +
            "               INNER JOIN book2genre bg ON b.id = bg.book_id " +
            "               INNER JOIN genres g      ON bg.genre_id = g.id " +
            "               INNER JOIN book2tag bt   ON bt.book_id = b.id " +
            "               INNER JOIN tags t        ON t.id = bt.tag_id " +
            "         WHERE b.id NOT IN (SELECT bi.id FROM books bi " +
            "                                         INNER JOIN book2user bu ON bi.id = bu.book_id " +
            "                             WHERE bu.user_id = :userId AND bu.type IN ('CART', 'PAID', 'KEPT')) " +
            "                    AND (a.id IN :authors OR g.id IN :genres OR t.id IN :tags) " +
            "         GROUP BY 1) tab_1 ON books.id = tab_1.bid " +
            " ORDER BY n DESC, pub_date DESC", nativeQuery = true)
    Page<Book> findBooksByAuthorInOrGenresInOrTagsInAndBookUserNotIn(List<Author> authors, List<Genre> genres, List<Tag> tags, Integer userId, Pageable nextPage);

    @Query(value =
            "SELECT id, description, image, is_bestseller, discount, price, pub_date, slug, title, author_id " +
            "  FROM books " +
            "       INNER JOIN " +
            "       (SELECT book_id, SUM(popul) AS popular " +
            "          FROM (SELECT book_id, " +
            "                       bu.type, " +
            "                       CASE " +
            "                       WHEN bu.type = 'PAID' THEN COUNT(*) " +
            "                       WHEN bu.type = 'CART' THEN COUNT(*) * 0.7 " +
            "                       WHEN bu.type = 'KEPT' THEN COUNT(*) * 0.4 " +
            "                       ELSE 0 END as popul " +
            "                  FROM book2user bu GROUP BY 1, 2) AS tab1 " +
            "         GROUP BY 1 ORDER BY 2 DESC) AS tab2 " +
            "       ON books.id = tab2.book_id " +
            " ORDER BY popular DESC", nativeQuery = true)
    List<Book> findPopularBooks(Pageable nextPage);

    @Query(value =
            "WITH tab_view_for_user AS " +
                    "(SELECT b2u.book_id as book_id FROM book2user b2u WHERE type = 'VIEWED' AND b2u.user_id = :user) " +
                    "SELECT id, description, image, is_bestseller, discount, price, pub_date, slug, title, author_id " +
                    "  FROM books " +
                    "       INNER JOIN (SELECT book_id, " +
                    "                          CASE WHEN book_id IN (SELECT book_id FROM tab_view_for_user) " +
                    "                          THEN (pop * 1.5 + 1) " +
                    "                          ELSE pop END as popular " +
                    "                     FROM (SELECT book_id, SUM(popul) AS pop " +
                    "                             FROM (SELECT book_id, " +
                    "                                          bu.type, " +
                    "                                          CASE " +
                    "                                          WHEN bu.type = 'PAID' THEN COUNT(*) " +
                    "                                          WHEN bu.type = 'CART' THEN COUNT(*) * 0.7 " +
                    "                                          WHEN bu.type = 'KEPT' THEN COUNT(*) * 0.4 " +
                    "                                          ELSE 0 END as popul " +
                    "                                     FROM book2user bu " +
                    "                                    GROUP BY 1, 2) AS tab_popular " +
                    "                    GROUP BY 1 ORDER BY 2 DESC) as tab_popular_all) as tab_popular_user " +
                    "        ON books.id = tab_popular_user.book_id " +
                    "  ORDER BY popular DESC",
            nativeQuery = true)
    List<Book> findPopularBooksForUser(BookstoreUser user, Pageable nextPage);
}
