package ru.onexteam.lottery.service;

import ru.onexteam.lottery.model.User;
import ru.onexteam.lottery.repository.UserRepository;
import ru.onexteam.lottery.security.JwtUtil;
import ru.onexteam.lottery.security.PasswordUtil;


import java.util.Optional;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();
    private final PasswordUtil passwordUtil = new PasswordUtil();

    public String register(String email, String password) { //Метод пытается провести регистрацию

        if (userRepository.findByEmail(email).isEmpty()){

            User user = new User();
            user.email = email;
            user.password = passwordUtil.hashPassword(password);;
            user.role = "USER";

            userRepository.save(user);

            return new JwtUtil().generateToken(user.id, user.role);
        }else {
            throw new RuntimeException("Пользователь с таким email@ уже существует ");
        }
    }

    public String login(String email, String password) { // Метод проверяет email и пароль на вход
        Optional<User> userOptional = userRepository.findByEmail(email);
         if(userOptional.isPresent()){
             if(passwordUtil.verifyPassword(password, userOptional.get().password)){
                 return new JwtUtil().generateToken(userOptional.get().id, userOptional.get().role);
             }else {
                 throw new RuntimeException("Неверный пароль");
             }
         }else {
             throw new RuntimeException("Пользователя с таким email@ не существует");
         }
    }
}