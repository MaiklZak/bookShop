package com.example.mybookshopapp.service;

import com.example.mybookshopapp.entity.BookFile;
import com.example.mybookshopapp.entity.FileDownload;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.repository.BookFileRepository;
import com.example.mybookshopapp.repository.FileDownloadRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Service
public class ResourceStorage {

    @Value("${upload.path}")
    String uploadPath;

    @Value("${download.path}")
    String downloadPath;

    private final BookFileRepository bookFileRepository;
    private final FileDownloadRepository fileDownloadRepository;

    @Autowired
    public ResourceStorage(BookFileRepository bookFileRepository, FileDownloadRepository fileDownloadRepository) {
        this.bookFileRepository = bookFileRepository;
        this.fileDownloadRepository = fileDownloadRepository;
    }

    public String saveNewBookImage(MultipartFile file, String slug) throws IOException {
        String resourceURI = null;

        if (!file.isEmpty()) {
            if (!new File(uploadPath).exists()) {
                Files.createDirectories(Paths.get(uploadPath));
                Logger.getLogger(this.getClass().getSimpleName()).info(() -> "created image folder in " + uploadPath);
            }

            String fileName = slug + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(uploadPath, fileName);
            resourceURI = "/book-covers/" + fileName;
            file.transferTo(path); // uploading user file here
            Logger.getLogger(this.getClass().getSimpleName()).info(() -> fileName + " uploaded OK!");
        }
        return resourceURI;
    }

    public Path getBookFilePath(String hash) {
        BookFile bookFile = bookFileRepository.findBookFileByHash(hash);
        return Paths.get(bookFile.getPath());
    }

    public MediaType getBookFileMime(String hash) {
        BookFile bookFile = bookFileRepository.findBookFileByHash(hash);
        String mimeType = URLConnection.guessContentTypeFromName(Paths.get(bookFile.getPath()).getFileName().toString());
        if (mimeType != null) {
            return MediaType.parseMediaType(mimeType);
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    public byte[] getBookFileByteArray(String hash) throws IOException {
        BookFile bookFile = bookFileRepository.findBookFileByHash(hash);
        Path path = Paths.get(downloadPath, bookFile.getPath());
        return Files.readAllBytes(path);
    }

    public void setCountDownloadBookForUser(BookstoreUser user, String hash) {
        BookFile fileByHash = bookFileRepository.findBookFileByHash(hash);
        FileDownload download = fileDownloadRepository.findByUserAndBook(user, fileByHash.getBook());
        if (download == null) {
            download = new FileDownload(user, fileByHash.getBook());
        } else {
            download.setCount(download.getCount() + 1);
        }
        fileDownloadRepository.save(download);
    }
}
