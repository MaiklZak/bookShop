package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.dto.ApiResponse;
import com.example.MyBookShopApp.data.model.Book;
import com.example.MyBookShopApp.errs.BookstoreApiWrongParameterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@Api(description = "book data api")
public class BooksRestApiController {

    private final BookService bookService;

    @Autowired
    public BooksRestApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/by-author")
    @ApiOperation("operation to get book list of bookshop by passed author first name")
    public ResponseEntity<List<Book>> booksByAuthor(@RequestParam("author") String authorName) throws BookstoreApiWrongParameterException {
        List<Book> books = bookService.getBooksByAuthor(authorName);
        addLinkToInBook(books);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/by-title")
    @ApiOperation("get books by book title")
    public ResponseEntity<ApiResponse<Book>> booksByTitle(@RequestParam("title") String title) throws BookstoreApiWrongParameterException {
        ApiResponse<Book> response = new ApiResponse<>();
        List<Book> data = bookService.getBooksByTitle(title);
        addLinkToInBook(data);
        response.setDebugMessage("successful request");
        response.setMessage("data size: " + data.size() + " elements");
        response.setStatus(HttpStatus.OK);
        response.setTimeStamp(LocalDateTime.now());
        response.setData(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books/by-price-range")
    @ApiOperation("get books by price range from min price to max price")
    public ResponseEntity<List<Book>> priceRangeBooks(@RequestParam("min") Integer min, @RequestParam("max") Integer max) throws BookstoreApiWrongParameterException {
        List<Book> books = bookService.getBooksWithPriceBetween(min, max);
        addLinkToInBook(books);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/with-max-discount")
    @ApiOperation("get list of book with max price")
    public ResponseEntity<List<Book>> maxPriceBooks() throws BookstoreApiWrongParameterException {
        List<Book> books = bookService.getBooksWithMaxPrice();
        addLinkToInBook(books);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/bestsellers")
    @ApiOperation("get bestseller books (which is_bestseller = 1)")
    public ResponseEntity<List<Book>> bestSellersBooks() throws BookstoreApiWrongParameterException {
        List<Book> books = bookService.getBestsellers();
        addLinkToInBook(books);
        return ResponseEntity.ok(books);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleMissingServletRequestParameterException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<Book>(HttpStatus.BAD_REQUEST, "Missing required parameters",
                exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookstoreApiWrongParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleBookstoreApiWrongParameterException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<Book>(HttpStatus.BAD_REQUEST, "Bad parameter value...", exception)
                , HttpStatus.BAD_REQUEST);
    }

    public void addLinkToInBook(List<Book> books) throws BookstoreApiWrongParameterException {
        for (Book book : books) {
            book.add(linkTo(methodOn(BooksRestApiController.class).booksByAuthor(book.getAuthor().getFirstName())).withRel("booksByAuthor"));
            book.add(linkTo(methodOn(BooksRestApiController.class).booksByTitle(book.getTitle())).withRel("booksByTitle"));
            book.add(linkTo(methodOn(BooksRestApiController.class).priceRangeBooks(0, Math.toIntExact(Math.round(book.getPriceOld())))).withRel("priceRangeBooks"));
            book.add(linkTo(methodOn(BooksRestApiController.class).maxPriceBooks()).withRel("maxPriceBooks"));
            book.add(linkTo(methodOn(BooksRestApiController.class).bestSellersBooks()).withRel("bestSellersBooks"));
        }
    }
}
