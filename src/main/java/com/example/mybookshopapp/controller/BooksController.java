package com.example.mybookshopapp.controller;

import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.ResourceStorage;
import com.example.mybookshopapp.entity.Book;
import com.example.mybookshopapp.entity.BookUserType;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.entity.security.BookstoreUserDetails;
import com.example.mybookshopapp.repository.security.BookstoreUserRepository;
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
import java.util.UUID;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksController {

    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private final BookRepository bookRepository;
    private final ResourceStorage storage;
    private final BookstoreUserRepository bookstoreUserRepository;
    private final BookService bookService;

    @Autowired
    public BooksController(BookRepository bookRepository, ResourceStorage storage, BookstoreUserRepository bookstoreUserRepository, BookService bookService) {
        this.bookRepository = bookRepository;
        this.storage = storage;
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.bookService = bookService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
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
            bookService.changeBookStatusForUser(book, user.getBookstoreUser(), BookUserType.VIEWED);
        } else {
            BookstoreUser bookstoreUserByHash = bookstoreUserRepository.findBookstoreUserByHash(userHash);
            if (userHash != null && !userHash.equals("") && bookstoreUserByHash != null) {
                bookService.changeBookStatusForUser(book, bookstoreUserByHash, BookUserType.VIEWED);
            } else {
                BookstoreUser defaultUser = new BookstoreUser();
                defaultUser.setHash(UUID.randomUUID().toString());
                defaultUser = bookstoreUserRepository.save(defaultUser);
                bookService.changeBookStatusForUser(book, defaultUser, BookUserType.VIEWED);

                Cookie cookie = new Cookie("userHash", defaultUser.getHash());
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        model.addAttribute("slugBook", book);
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
}
