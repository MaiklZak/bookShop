package com.example.mybookshopapp.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "book_file")
public class BookFile implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_book_file_id", sequenceName = "seq_book_file_id", initialValue = 1001, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_book_file_id")
    private Integer id;

    private String hash;

    @Column(name = "type_id")
    private Integer typeId;

    private String path;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    public String getBookFileExtensionString() {
        return BookFileType.getExtensionStringByTypeId(typeId);
    }

    public void setType(String fileName) {
        if (fileName.endsWith(".pdf")) {
            setTypeId(1);
        } else if (fileName.endsWith(".epub")) {
            setTypeId(2);
        } else {
            setTypeId(3);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
