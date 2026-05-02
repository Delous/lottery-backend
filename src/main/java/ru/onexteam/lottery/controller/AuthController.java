package ru.onexteam.lottery.controller;

import io.javalin.Javalin;
import ru.onexteam.lottery.dto.auth.AuthResponse;
import ru.onexteam.lottery.dto.auth.LoginRequest;
import ru.onexteam.lottery.dto.auth.RegisterRequest;
import ru.onexteam.lottery.security.JwtUtil;
import ru.onexteam.lottery.service.AuthService;

public class AuthController {

    private static final JwtUtil jwtUtil = new JwtUtil();

    public static void register(Javalin app, AuthService service) {
        app.post("/api/auth/register", ctx -> {
            try {
                RegisterRequest request = ctx.bodyAsClass(RegisterRequest.class);
                validateCredentials(request.email, request.password);

                String token = service.register(request.email, request.password);
                ctx.status(201).json(new AuthResponse(token, jwtUtil.extractRole(token)));
            } catch (IllegalArgumentException ex) {
                ctx.status(400).result(ex.getMessage());
            } catch (RuntimeException ex) {
                ctx.status(409).result(ex.getMessage());
            }
        });

        app.post("/api/auth/login", ctx -> {
            try {
                LoginRequest request = ctx.bodyAsClass(LoginRequest.class);
                validateCredentials(request.email, request.password);

                String token = service.login(request.email, request.password);
                ctx.status(200).json(new AuthResponse(token, jwtUtil.extractRole(token)));
            } catch (IllegalArgumentException ex) {
                ctx.status(400).result(ex.getMessage());
            } catch (RuntimeException ex) {
                ctx.status(401).result(ex.getMessage());
            }
        });
    }

    private static void validateCredentials(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Требуется email");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Требуется пароль");
        }
    }
}
