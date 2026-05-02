package ru.onexteam.lottery.security;

import com.password4j.Password;

public class PasswordUtil {

    public String hashPassword(String password) {
        return Password.hash(password)
                .withArgon2()
                .getResult();
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return Password.check(rawPassword, hashedPassword)
                .withArgon2();
    }
}