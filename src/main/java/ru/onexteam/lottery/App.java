package ru.onexteam.lottery;

import io.javalin.Javalin;
import ru.onexteam.lottery.config.DbConfig;
import ru.onexteam.lottery.controller.AuthController;
import ru.onexteam.lottery.controller.DocsController;
import ru.onexteam.lottery.controller.DrawController;
import ru.onexteam.lottery.controller.TicketController;
import ru.onexteam.lottery.security.AuthMiddleware;
import ru.onexteam.lottery.security.JwtUtil;
import ru.onexteam.lottery.service.AuthService;

public class App {

    public static void main(String[] args) {
        DbConfig.initialize();

        AuthService authService = new AuthService();
        authService.ensureAdmin(DbConfig.getProperty("app.admin.email"), DbConfig.getProperty("app.admin.password"));

        Javalin app = Javalin.create().start(8080);

        DocsController.register(app);
        AuthController.register(app, authService);

        JwtUtil jwtUtil = new JwtUtil();
        new AuthMiddleware(jwtUtil).register(app);

        DrawController.register(app);
        TicketController.register(app);

        System.out.println("Сервер запущен: http://localhost:8080");
    }
}
