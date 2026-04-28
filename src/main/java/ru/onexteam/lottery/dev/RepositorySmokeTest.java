package ru.onexteam.lottery.dev;

import ru.onexteam.lottery.config.DbConfig;
import ru.onexteam.lottery.model.User;
import ru.onexteam.lottery.repository.UserRepository;

public class RepositorySmokeTest {

    // Тест работы репозитория и подключения к БД
    public static void main(String[] args) {
        DbConfig.initialize();

        var userRepository = new UserRepository();
        var email = "smoke-test-" + System.currentTimeMillis() + "@test.local";

        var user = new User();
        user.email = email;
        user.password = "123456";
        user.role = "USER";

        userRepository.save(user);

        var savedUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден после сохранения"));

        System.out.println("Smoke test успешно пройден");
        System.out.println("Сохраненный user id: " + savedUser.id);
        System.out.println("Сохраненный user email: " + savedUser.email);
        System.out.println("Сохраненная роль: " + savedUser.role);
    }
}
