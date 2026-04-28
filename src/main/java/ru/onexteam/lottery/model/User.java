package ru.onexteam.lottery.model;

public class User {
    public Long id;
    public String email;
    public String password;
    public String role; // USER / ADMIN

    public User() {
    }

    public User(Long id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}