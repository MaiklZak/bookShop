package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.ResourceStorage;
import com.example.MyBookShopApp.data.model.Book;
import com.example.MyBookShopApp.data.model.BookUser;
import com.example.MyBookShopApp.data.model.BookUserType;
import com.example.MyBookShopApp.data.repositories.BookRepository;
import com.example.MyBookShopApp.data.repositories.BookUserRepository;
import com.example.MyBookShopApp.security.BookstoreUserDetails;
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

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookRepository bookRepository;
    private final ResourceStorage storage;
    private final BookUserRepository bookUserRepository;

    @Autowired
    public BooksController(BookRepository bookRepository, ResourceStorage storage, BookUserRepository bookUserRepository) {
        this.bookRepository = bookRepository;
        this.storage = storage;
        this.bookUserRepository = bookUserRepository;
    }

    @GetMapping("/{slug}")
    public String bookPage(@AuthenticationPrincipal BookstoreUserDetails user,
                           @PathVariable("slug") String slug,
                           Model model) {
        Book book = bookRepository.findBookBySlug(slug);
        if (book == null) {
            return "redirect:/index";
        }
        BookUser bookUser = bookUserRepository.findByBook(book);
        if (bookUser == null && user != null) {
            bookUser = new BookUser(BookUserType.VIEWED, book, user.getBookstoreUser());
            bookUserRepository.save(bookUser);
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
}
