package com.example.MyBookShopApp.data.model;

import javax.persistence.*;

@Entity
@Table(name = "book2user_type")
public class BookUserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "code")
    private TypeBindingBookToUser type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeBindingBookToUser getType() {
        return type;
    }

    public void setType(TypeBindingBookToUser type) {
        this.type = type;
    }
}
