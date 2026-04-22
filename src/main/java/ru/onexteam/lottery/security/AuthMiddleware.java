package ru.onexteam.lottery.security;

import io.javalin.Javalin;

public class AuthMiddleware {

    private final JwtUtil jwtUtil;

    public AuthMiddleware(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void register(Javalin app) {
        // добавить before-handler
    }
}