package ru.onexteam.lottery;

import io.javalin.Javalin;
import ru.onexteam.lottery.controller.AuthController;
import ru.onexteam.lottery.controller.DrawController;
import ru.onexteam.lottery.controller.TicketController;
import ru.onexteam.lottery.security.AuthMiddleware;
import ru.onexteam.lottery.security.JwtUtil;
import ru.onexteam.lottery.service.AuthService;

public class App {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8080);

        AuthService authService = new AuthService();
        AuthController.register(app, authService);

        JwtUtil jwtUtil = new JwtUtil();
        new AuthMiddleware(jwtUtil).register(app);

        DrawController.register(app);
        TicketController.register(app);

        System.out.println("Server started on http://localhost:8080");
    }

}