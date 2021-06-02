package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.ResourceStorage;
import com.example.MyBookShopApp.data.dto.SearchWordDto;
import com.example.MyBookShopApp.data.model.Book;
import com.example.MyBookShopApp.data.model.BookReview;
import com.example.MyBookShopApp.data.model.BookReviewLike;
import com.example.MyBookShopApp.data.repositories.BookRepository;
import com.example.MyBookShopApp.data.repositories.BookReviewLikeRepository;
import com.example.MyBookShopApp.data.repositories.BookReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final BookReviewRepository bookReviewRepository;
    private final BookRepository bookRepository;
    private final ResourceStorage storage;

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @Autowired
    public BooksController(BookReviewLikeRepository bookReviewLikeRepository, BookReviewRepository bookReviewRepository, BookRepository bookRepository, ResourceStorage storage) {
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.bookReviewRepository = bookReviewRepository;
        this.bookRepository = bookRepository;
        this.storage = storage;
    }

    @GetMapping("/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("slugBook", bookRepository.findBookBySlug(slug));
        List<BookReview> bookReviewList = bookReviewRepository.findAllByBookSlug(slug);
        bookReviewList.sort(Comparator.comparing(br -> br.getDisLikes() - br.getLikes()));
        model.addAttribute("reviewsOfBook", bookReviewList);
        return "/books/slug";
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug) throws IOException {
        String savePath = storage.saveNewBookImage(file, slug);
        Book bookToUpdate = bookRepository.findBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate); //save new path in db here
        return ("redirect:/books/" + slug);
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException {
        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);

        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: " + mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: " + data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @PostMapping("/{slug}/bookReview")
    public String addReview(@PathVariable("slug") String slug, @RequestParam("text") String text) {
        BookReview bookReview = new BookReview(bookRepository.findBookBySlug(slug), text);
        bookReviewRepository.save(bookReview);
        return "redirect:/books/" + slug;
    }

//    @PostMapping("/{slug}/rateBookReview/{reviewId}")
//    public String rateBookReview(@PathVariable("slug") String slug,
//                                 @PathVariable("reviewId") Integer reviewId,
//                                 @RequestParam("value") Integer value) {
//        bookReviewLikeRepository.save(new BookReviewLike(bookReviewRepository.findById(reviewId).get(), value));
//        return "redirect:/books/" + slug;
//    }

    @PostMapping("/{slug}/rateBookReview/{reviewId}")
    public String rateBookReview(
            @PathVariable("slug") String slug,
            @PathVariable("reviewId") Integer reviewId,
            @RequestParam("value") Integer value,
            @CookieValue(name = "rateReviewContents", required = false) String rateReviewContents,
            HttpServletResponse response
    ) {
        if (rateReviewContents == null || rateReviewContents.equals("")) {
            BookReviewLike bookReviewLike = new BookReviewLike(bookReviewRepository.findById(reviewId).get(), value);
            bookReviewLikeRepository.save(bookReviewLike);
            Cookie cookie = new Cookie("rateReviewContents", "/" + reviewId + "=" + value + ":" + bookReviewLike.getId());
            cookie.setPath("/books");
            response.addCookie(cookie);
        } else if (!rateReviewContents.contains(reviewId + "=")) {
            BookReviewLike bookReviewLike = new BookReviewLike(bookReviewRepository.findById(reviewId).get(), value);
            bookReviewLikeRepository.save(bookReviewLike);
            StringJoiner stringJoiner = new StringJoiner("/");
            Cookie cookie = new Cookie("rateReviewContents",
                    stringJoiner.add(rateReviewContents).add(reviewId + "=" + value + ":" + bookReviewLike.getId()).toString());
            cookie.setPath("/books");
            response.addCookie(cookie);
        } else {
            if (rateReviewContents.contains(reviewId + "=" + value)) {
                String[] elementsCookie = rateReviewContents.split("/");
                String element = Arrays.stream(elementsCookie)
                        .filter(s -> s.contains(reviewId + "=" + value))
                        .findFirst().get();
                bookReviewLikeRepository.deleteById(Integer.valueOf(element.split(":")[1]));
                Cookie cookie = new Cookie("rateReviewContents", rateReviewContents.replace("/" + element, ""));
                cookie.setPath("/books");
                response.addCookie(cookie);
            } else {
                String[] elementsCookie = rateReviewContents.split("/");
                String element = Arrays.stream(elementsCookie)
                        .filter(s -> s.contains(reviewId + "="))
                        .findFirst().get();
                BookReviewLike bookReviewLike = bookReviewLikeRepository.findById(Integer.valueOf(element.split(":")[1])).get();
                bookReviewLike.setValue(value);
                bookReviewLikeRepository.save(bookReviewLike);
                Cookie cookie = new Cookie("rateReviewContents",
                        rateReviewContents.replace(element, reviewId + "=" + value + ":" + bookReviewLike.getId()));
                cookie.setPath("/books");
                response.addCookie(cookie);
            }
        }
        return "redirect:/books/" + slug;
    }
}
