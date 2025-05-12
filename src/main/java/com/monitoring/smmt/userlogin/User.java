package com.monitoring.smmt.userlogin;

import jakarta.persistence.*;

@Entity
@Table(name = "users") //map to DB user table

//Pojo class
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) //maps to column
    private String username;

    @Column(nullable = false) //maps to column
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

