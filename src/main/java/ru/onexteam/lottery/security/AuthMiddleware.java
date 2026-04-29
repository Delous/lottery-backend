package ru.onexteam.lottery.security;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class AuthMiddleware {

    private final JwtUtil jwtUtil;

    public AuthMiddleware(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void register(Javalin app) {

        /*
         * Общая схема:
         *
         * /api/admin/*   -> только ADMIN
         * /api/user/*    -> USER и ADMIN
         */

        // Только админ
        app.before("/api/admin/*", this::authorizeAdmin);

        // Обычный пользователь (и админ тоже может)
        app.before("/api/user/*", this::authorizeUser);
    }

    /**
     * USER или ADMIN
     */
    private void authorizeUser(Context ctx) {
        String token = extractToken(ctx);

        if (token == null) {
            ctx.status(401).result("Authorization token required");
            return;
        }

        if (!jwtUtil.validateToken(token)) {
            ctx.status(401).result("Invalid token");
            return;
        }

        Long userId = jwtUtil.extractUserId(token);
        String role = jwtUtil.extractRole(token);

        if (!"USER".equals(role) && !"ADMIN".equals(role)) {
            ctx.status(403).result("Access denied");
            return;
        }

        ctx.attribute("userId", userId);
        ctx.attribute("role", role);
    }

    /**
     * Только ADMIN
     */
    private void authorizeAdmin(Context ctx) {
        String token = extractToken(ctx);

        if (token == null) {
            ctx.status(401).result("Authorization token required");
            return;
        }

        if (!jwtUtil.validateToken(token)) {
            ctx.status(401).result("Invalid token");
            return;
        }

        Long userId = jwtUtil.extractUserId(token);
        String role = jwtUtil.extractRole(token);

        if (!"ADMIN".equals(role)) {
            ctx.status(403).result("Admin access required");
            return;
        }

        ctx.attribute("userId", userId);
        ctx.attribute("role", role);
    }

    private String extractToken(Context ctx) {
        String header = ctx.header("Authorization");

        if (header == null || header.isBlank()) {
            return null;
        }

        return header.replace("Bearer ", "").trim();
    }

}