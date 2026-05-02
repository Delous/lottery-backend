package ru.onexteam.lottery.service;

import ru.onexteam.lottery.model.User;
import ru.onexteam.lottery.repository.UserRepository;
import ru.onexteam.lottery.security.JwtUtil;
import ru.onexteam.lottery.security.PasswordUtil;

import java.util.Optional;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();
    private final PasswordUtil passwordUtil = new PasswordUtil();
    private final JwtUtil jwtUtil = new JwtUtil();

    public String register(String email, String password) {
        String normalizedEmail = email.trim().toLowerCase();

        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User user = new User();
        user.email = normalizedEmail;
        user.password = passwordUtil.hashPassword(password);
        user.role = "USER";

        userRepository.save(user);
        return jwtUtil.generateToken(user.id, user.role);
    }

    public String login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email.trim().toLowerCase());

        if (userOptional.isEmpty() || !passwordUtil.verifyPassword(password, userOptional.get().password)) {
            throw new RuntimeException("Неверный email или пароль");
        }

        User user = userOptional.get();
        return jwtUtil.generateToken(user.id, user.role);
    }

    public void ensureAdmin(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return;
        }

        String normalizedEmail = email.trim().toLowerCase();
        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            return;
        }

        User user = new User();
        user.email = normalizedEmail;
        user.password = passwordUtil.hashPassword(password);
        user.role = "ADMIN";

        userRepository.save(user);
    }
}
