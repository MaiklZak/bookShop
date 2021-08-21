package com.example.mybookshopapp.service;

import com.example.mybookshopapp.entity.Book;
import com.example.mybookshopapp.errs.BookstoreApiWrongParameterException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookServiceTests {

    @Value("Diana")
    String bookTitle;

    private final BookService bookService;

    @Autowired
    BookServiceTests(BookService bookService) {
        this.bookService = bookService;
    }

    @Test
    void getBookData() {
        List<Book> bookList = bookService.getBestsellers();
        assertNotNull(bookList);
        assertFalse(bookList.isEmpty());
    }

    @Test
    void getBooksByTitle() throws BookstoreApiWrongParameterException {
        List<Book> bookListByTitle = bookService.getBooksByTitle(bookTitle);
        assertNotNull(bookListByTitle);
        assertFalse(bookListByTitle.isEmpty());
        for (Book book : bookListByTitle) {
            Logger.getLogger(this.getClass().getSimpleName()).info(book.toString());
            assertThat(book.getTitle()).contains(bookTitle);
        }
    }

    @Test()
    void getBookByTitleException() {
        BookstoreApiWrongParameterException exceptionEmptyTitle = assertThrows(BookstoreApiWrongParameterException.class,
                () -> bookService.getBooksByTitle(""));
        BookstoreApiWrongParameterException exceptionLengthTitleLessThan2 = assertThrows(BookstoreApiWrongParameterException.class,
                () -> bookService.getBooksByTitle("a"));
        BookstoreApiWrongParameterException exceptionTitleNoFound = assertThrows(BookstoreApiWrongParameterException.class,
                () -> bookService.getBooksByTitle("NoFound"));
        assertEquals("Wrong values passed to one or more parameters", exceptionEmptyTitle.getMessage());
        assertEquals("Wrong values passed to one or more parameters", exceptionLengthTitleLessThan2.getMessage());
        assertEquals("No data found with specified parameters...", exceptionTitleNoFound.getMessage());
    }

    @Test
    void getBooksWithPriceBetween() {
        List<Book> bookListByPriceBetween = bookService.getBooksWithPriceBetween(500, 1000);
        assertNotNull(bookListByPriceBetween);
        assertFalse(bookListByPriceBetween.isEmpty());
        for (Book book : bookListByPriceBetween) {
            assertThat(book.getPriceOld()).isGreaterThanOrEqualTo(500);
            assertThat(book.getPriceOld()).isLessThanOrEqualTo(1000);
        }
    }

    @Test
    void getBooksWithMaxPrice() {
        List<Book> bookListByMaxPrice = bookService.getBooksWithMaxPrice();
        assertNotNull(bookListByMaxPrice);
        assertFalse(bookListByMaxPrice.isEmpty());
        for (Book book : bookListByMaxPrice) {
            assertEquals(0.5, book.getPrice());
        }
    }

    @Test
    void getBestsellers() {
        List<Book> bookListByBestsellersEquals1 = bookService.getBestsellers();
        assertNotNull(bookListByBestsellersEquals1);
        assertFalse(bookListByBestsellersEquals1.isEmpty());
        for (Book book : bookListByBestsellersEquals1) {
            assertEquals(1, book.getIsBestseller());
        }
    }

    @Test
    void getPageOfSearchResultBooks() {
        List<Book> bookListByTitle = bookService.getPageOfSearchResultBooks(bookTitle, 0, 5).getContent();
        assertNotNull(bookListByTitle);
        assertFalse(bookListByTitle.isEmpty());
        assertThat(bookListByTitle.size()).isLessThanOrEqualTo(6);
        for (Book book : bookListByTitle) {
            assertThat(book.getTitle()).contains(bookTitle);
        }
    }
}