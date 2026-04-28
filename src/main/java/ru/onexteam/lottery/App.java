package ru.onexteam.lottery;

import io.javalin.Javalin;
import ru.onexteam.lottery.controller.AuthController;
import ru.onexteam.lottery.config.DbConfig;
import ru.onexteam.lottery.service.AuthService;

public class App {
    public static void main(String[] args) {
        DbConfig.initialize();

        Javalin app = Javalin.create().start(7000);

        AuthService authService = new AuthService();
        AuthController.register(app, authService);
    }
}