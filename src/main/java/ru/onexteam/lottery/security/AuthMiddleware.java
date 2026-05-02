package ru.onexteam.lottery.security;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;

public class AuthMiddleware {

    private final JwtUtil jwtUtil;

    public AuthMiddleware(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void register(Javalin app) {
        app.before("/api/admin/*", this::authorizeAdmin);
        app.before("/api/user/*", this::authorizeUser);
    }

    private void authorizeUser(Context ctx) {
        authorize(ctx, false);
    }

    private void authorizeAdmin(Context ctx) {
        authorize(ctx, true);
    }

    private void authorize(Context ctx, boolean adminOnly) {
        String token = extractToken(ctx);

        if (token == null) {
            throw new UnauthorizedResponse("Требуется токен авторизации");
        }

        if (!jwtUtil.validateToken(token)) {
            throw new UnauthorizedResponse("Недействительный токен");
        }

        Long userId = jwtUtil.extractUserId(token);
        String role = jwtUtil.extractRole(token);

        if (adminOnly && !"ADMIN".equals(role)) {
            throw new ForbiddenResponse("Требуется доступ администратора");
        }

        if (!adminOnly && !"USER".equals(role) && !"ADMIN".equals(role)) {
            throw new ForbiddenResponse("Доступ запрещен");
        }

        ctx.attribute("userId", userId);
        ctx.attribute("role", role);
    }

    private String extractToken(Context ctx) {
        String header = ctx.header("Authorization");

        if (header == null || header.isBlank()) {
            return null;
        }

        if (!header.startsWith("Bearer ")) {
            return null;
        }

        return header.substring("Bearer ".length()).trim();
    }
}
