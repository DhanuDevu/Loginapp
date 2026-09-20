package com.reglog.auth.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "JWT_tokens")
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tid")
    private Long tid;

    @Column(name = "uid", nullable = false)
    private Long uid;

    @Column(name = "token", nullable = false, length = 512)
    private String token;

    // cat: created_at timestamp
    @Column(name = "cat", nullable = false)
    private LocalDateTime cat;

    // eat: expires_at timestamp
    @Column(name = "eat", nullable = false)
    private LocalDateTime eat;

    public JwtToken() {
    }

    public JwtToken(Long uid, String token, LocalDateTime cat, LocalDateTime eat) {
        this.uid = uid;
        this.token = token;
        this.cat = cat;
        this.eat = eat;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCat() {
        return cat;
    }

    public void setCat(LocalDateTime cat) {
        this.cat = cat;
    }

    public LocalDateTime getEat() {
        return eat;
    }

    public void setEat(LocalDateTime eat) {
        this.eat = eat;
    }
}
