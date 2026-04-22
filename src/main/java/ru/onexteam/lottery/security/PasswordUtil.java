package ru.onexteam.lottery.security;

public class PasswordUtil {

    public String hashPassword(String password) {
        return password;
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return rawPassword.equals(hashedPassword);
    }
}