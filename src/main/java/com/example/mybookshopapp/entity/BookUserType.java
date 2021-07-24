package com.example.mybookshopapp.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "book2user_type")
public class BookUserType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TypeBookToUser code;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TypeBookToUser getCode() {
        return code;
    }

    public void setCode(TypeBookToUser code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
