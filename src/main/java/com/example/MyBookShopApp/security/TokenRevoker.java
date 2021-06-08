package com.example.MyBookShopApp.security;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "token_blacklist")
public class TokenRevoker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String value;
    private Date expiration;

    public TokenRevoker(String value, Date expiration) {
        this.value = value;
        this.expiration = expiration;
    }

    public TokenRevoker() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
}
