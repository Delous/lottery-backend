package ru.onexteam.lottery.service;

import ru.onexteam.lottery.model.User;
import ru.onexteam.lottery.repository.UserRepository;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();

    public String register(String email, String password) {
        User user = new User();
        user.email = email;
        user.password = password;
        user.role = "USER";

        userRepository.save(user);
        return "registered";
    }

    public String login(String email, String password) {
        return "token";
    }
}