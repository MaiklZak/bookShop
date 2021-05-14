package com.example.MyBookShopApp.data.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String hash;

    @Column(name = "reg_time")
    private Date regTime;

    private Integer balance;
    private String name;

    @OneToMany(mappedBy = "user")
    private Set<BookUser> bookUser;

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

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<BookUser> getBookUser() {
        return bookUser;
    }

    public void setBookUser(Set<BookUser> bookUser) {
        this.bookUser = bookUser;
    }
}
