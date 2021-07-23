package com.example.mybookshopapp.entity.security;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_contact")
public class UserContact implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_bok_contact", sequenceName = "seq_bok_contact", initialValue = 1001, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bok_contact")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private BookstoreUser user;

    @Enumerated(EnumType.STRING)
    private ContactType type;

    @Column(columnDefinition = "SMALLINT")
    private Integer approved;

    private String code;

    @Column(name = "code_trials")
    private Integer codeTrials;

    @Column(name = "code_time", columnDefinition = "TIMESTAMP(6)")
    private LocalDateTime codeTime;

    private String contact;

    public UserContact(BookstoreUser user, String contact, Integer approved, ContactType type) {
        this.user = user;
        this.type = type;
        this.approved = approved;
        this.contact = contact;
    }

    public UserContact(String code, String contact) {
        this();
        this.code = code;
        this.contact = contact;
    }

    public UserContact() {
        this.codeTime = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BookstoreUser getUser() {
        return user;
    }

    public void setUser(BookstoreUser user) {
        this.user = user;
    }

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCodeTrials() {
        return codeTrials;
    }

    public void setCodeTrials(Integer codeTrials) {
        this.codeTrials = codeTrials;
    }

    public LocalDateTime getCodeTime() {
        return codeTime;
    }

    public void setCodeTime(LocalDateTime codeTime) {
        this.codeTime = codeTime;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
