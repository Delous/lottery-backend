package ru.onexteam.lottery.controller;

import io.javalin.Javalin;
import ru.onexteam.lottery.dto.draw.CreateDrawRequest;
import ru.onexteam.lottery.service.DrawService;

public class DrawController {

    private static final DrawService drawService = new DrawService();

    public static void register(Javalin app) {

        app.post("/api/draws", ctx -> {
            // TODO: AuthMiddleware проверка роли (требуемое значение "admin").
            CreateDrawRequest req = ctx.bodyAsClass(CreateDrawRequest.class);
            String result = drawService.createDraw(req.title);
            ctx.status(201).json(result);
        });

        app.get("/api/draws/active", ctx -> {
            ctx.json(drawService.getActiveDraws());
        });

    }

}
