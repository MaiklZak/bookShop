package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.Book;
import com.example.mybookshopapp.entity.BookUserType;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
