package ru.onexteam.lottery.security;

import io.javalin.Javalin;

public class AuthMiddleware {

    private final JwtUtil jwtUtil;

    public AuthMiddleware(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void register(Javalin app) {
        app.before("/admin/*", ctx -> { // Поменять /admin/* на ссылку по факту
            String token = ctx.header("Authorization");
            if(token == null){
                // Ошибка нужно авторизоваться
            }else if (!jwtUtil.validateToken(token)) {
                // Ошибка токен не верный
            }

        });
        app.get("/admin/*", ctx -> {
            String token = ctx.header("Authorization"); // Поменять /admin/* на ссылку по факту
            Long userId = jwtUtil.extractUserId(token);
            String userRole = jwtUtil.extractRole(token);
            // Куда вернуть?
        });
        // добавить before-handler
    }
}