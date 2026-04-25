package ru.onexteam.lottery.security;
//import org.eclipse.jetty.util.security.Password;
//не понимаю зачем эта библиотека
import com.password4j.Password;

public class PasswordUtil {

    public String hashPassword(String password) {
        String hash = Password.hash(password)
                .withArgon2()
                .getResult();
        return hash;
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        boolean verified = Password.check(rawPassword, hashedPassword)
                .withArgon2();
        //return rawPassword.equals(hashedPassword);
        return verified;
    }
}