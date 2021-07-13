package com.example.mybookshopapp.controller;

import com.example.mybookshopapp.dto.RatingBookDto;
import com.example.mybookshopapp.dto.ReviewDto;
import com.example.mybookshopapp.dto.ReviewLikeDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.*;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.entity.security.BookstoreUserDetails;
import com.example.mybookshopapp.repository.BookRatingRepository;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.repository.BookReviewLikeRepository;
import com.example.mybookshopapp.repository.BookReviewRepository;
import com.example.mybookshopapp.repository.security.BookstoreUserRepository;
import com.example.mybookshopapp.service.BookUserService;
import com.example.mybookshopapp.service.ResourceStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksController {

    private static final String REDIRECT_BOOKS_URL = "redirect:/books/";

    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private final BookRatingRepository bookRatingRepository;
    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final BookReviewRepository bookReviewRepository;
    private final BookRepository bookRepository;
    private final ResourceStorage storage;
    private final BookstoreUserRepository bookstoreUserRepository;
    private final BookUserService bookUserService;

    @Autowired
    public BooksController(BookRatingRepository bookRatingRepository, BookReviewLikeRepository bookReviewLikeRepository, BookReviewRepository bookReviewRepository, BookRepository bookRepository, ResourceStorage storage, BookstoreUserRepository bookstoreUserRepository, BookUserService bookUserService) {
        this.bookRatingRepository = bookRatingRepository;
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.bookReviewRepository = bookReviewRepository;
        this.bookRepository = bookRepository;
        this.storage = storage;
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.bookUserService = bookUserService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("curUsr")
    public BookstoreUser getCurrentUser(@AuthenticationPrincipal BookstoreUserDetails userDetails) {
        if (userDetails != null) {
            return userDetails.getBookstoreUser();
        }
        return null;
    }

    @GetMapping("/{slug}")
    public String bookPage(@AuthenticationPrincipal BookstoreUserDetails user,
                           @PathVariable("slug") String slug,
                           @CookieValue(value = "userHash", required = false) String userHash,
                           HttpServletResponse response,
                           Model model) {
        Book book = bookRepository.findBookBySlug(slug);
        if (book == null) {
            return "redirect:/index";
        }
        if (user != null) {
            bookUserService.changeBookStatusToViewedForUser(slug, user.getBookstoreUser());
            model.addAttribute("ratingBook", bookRatingRepository.findByBookAndUser(book, user.getBookstoreUser()));
        } else {
            BookstoreUser bookstoreUserByHash = bookstoreUserRepository.findBookstoreUserByHash(userHash);
            if (userHash != null && !userHash.equals("") && bookstoreUserByHash != null) {
                bookUserService.changeBookStatusToViewedForUser(slug, bookstoreUserByHash);
            } else {
                BookstoreUser defaultUser = new BookstoreUser();
                defaultUser.setHash(UUID.randomUUID().toString());
                defaultUser = bookstoreUserRepository.save(defaultUser);
                bookUserService.changeBookStatusToViewedForUser(slug, defaultUser);

                Cookie cookie = new Cookie("userHash", defaultUser.getHash());
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        List<BookReview> bookReviewList = bookReviewRepository.findAllByBookSlug(slug);
        bookReviewList.sort(Comparator.comparing(br -> br.getDisLikes() - br.getLikes()));
        model.addAttribute("reviewsOfBook", bookReviewList);
        model.addAttribute("slugBook", book);
        return "/books/slug";
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug) throws IOException {
        String savePath = storage.saveNewBookImage(file, slug);
        Book bookToUpdate = bookRepository.findBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate); //save new path in db here
        return REDIRECT_BOOKS_URL + slug;
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@AuthenticationPrincipal BookstoreUserDetails userDetails,
                                                      @PathVariable("hash") String hash) throws IOException {

        storage.setCountDownloadBookForUser(userDetails.getBookstoreUser(), hash);

        Path path = storage.getBookFilePath(hash);
        logger.info(() -> "book file path: {}" + path);

        MediaType mediaType = storage.getBookFileMime(hash);
        logger.info(() -> "book file mime type: " + mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        logger.info(() -> "book file data len: " + data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @PostMapping("/bookReview")
    public String addReview(@AuthenticationPrincipal BookstoreUserDetails userDetails,
                            @RequestBody ReviewDto payload) {
        Book book = bookRepository.getOne(payload.getBookId());
        BookReview bookReview = new BookReview(
                userDetails.getBookstoreUser(), book, payload.getText());
        bookReviewRepository.save(bookReview);
        return REDIRECT_BOOKS_URL + book.getSlug();
    }

    @PostMapping("/{slug}/rateBookReview")
    public String rateBookReview(@AuthenticationPrincipal BookstoreUserDetails userDetails,
                                 @PathVariable String slug,
                                 @RequestBody ReviewLikeDto payload) {
        BookReview bookReview = bookReviewRepository.getOne(payload.getReviewid());

        BookReviewLike likeByReviewAndUser = bookReviewLikeRepository.findByBookReviewAndUser(bookReview, userDetails.getBookstoreUser());
        if (likeByReviewAndUser != null) {
            if (!likeByReviewAndUser.getValue().equals(payload.getValue())) {
                BookReviewLike newBookReviewLike = new BookReviewLike(userDetails.getBookstoreUser(), bookReview, payload.getValue());
                bookReviewLikeRepository.save(newBookReviewLike);
            }
            bookReviewLikeRepository.delete(likeByReviewAndUser);
        } else {
            BookReviewLike bookReviewLike = new BookReviewLike(userDetails.getBookstoreUser(), bookReview, payload.getValue());
            bookReviewLikeRepository.save(bookReviewLike);
        }
        return REDIRECT_BOOKS_URL + slug;
    }

    @PostMapping("/toRating")
    public String toRatingBook(@AuthenticationPrincipal BookstoreUserDetails userDetails,
                               @RequestBody RatingBookDto payload) {

        Book book = bookRepository.getOne(payload.getBookId());
        BookRating ratingByBookAndUser = bookRatingRepository.findByBookAndUser(book, userDetails.getBookstoreUser());
        if (ratingByBookAndUser != null) {
            ratingByBookAndUser.setValue(payload.getValue());
            bookRatingRepository.save(ratingByBookAndUser);
        } else {
            BookRating newBookRating = new BookRating(userDetails.getBookstoreUser(), book, payload.getValue());
            bookRatingRepository.save(newBookRating);
        }
        return REDIRECT_BOOKS_URL + book.getSlug();
    }
}
