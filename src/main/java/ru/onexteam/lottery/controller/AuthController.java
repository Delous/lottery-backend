package ru.onexteam.lottery.controller;

import io.javalin.Javalin;
import ru.onexteam.lottery.service.AuthService;

public class AuthController {

    public static void register(Javalin app, AuthService service) {

        // Регистрация пользователя.
        app.post("/api/auth/register", ctx -> {

            try {
                String email = ctx.formParam("email");
                String password = ctx.formParam("password");

                if (email == null || email.isBlank()) {
                    ctx.status(400).result("Email is required");
                    return;
                }

                if (password == null || password.isBlank()) {
                    ctx.status(400).result("Password is required");
                    return;
                }

                String token = service.register(email, password);

                ctx.status(201).result(token);

            } catch (RuntimeException ex) {
                ctx.status(409).result(ex.getMessage());
            }
        });


        // Авторизация пользователя.
        app.post("/api/auth/login", ctx -> {

            try {
                String email = ctx.formParam("email");
                String password = ctx.formParam("password");

                if (email == null || email.isBlank()) {
                    ctx.status(400).result("Email is required");
                    return;
                }

                if (password == null || password.isBlank()) {
                    ctx.status(400).result("Password is required");
                    return;
                }

                String token = service.login(email, password);

                ctx.status(200).result(token);

            } catch (RuntimeException ex) {
                ctx.status(401).result(ex.getMessage());
            }
        });
    }

}
