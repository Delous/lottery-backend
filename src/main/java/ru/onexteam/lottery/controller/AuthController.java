package ru.onexteam.lottery.controller;

import io.javalin.Javalin;
import ru.onexteam.lottery.service.AuthService;

public class AuthController {

    public static void register(Javalin app, AuthService service) {

        app.post("/auth/register", ctx -> {
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");

            String result = service.register(email, password);
            ctx.result(result);
        });

        app.post("/auth/login", ctx -> {
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");

            String token = service.login(email, password);
            ctx.result(token);
        });
    }
}