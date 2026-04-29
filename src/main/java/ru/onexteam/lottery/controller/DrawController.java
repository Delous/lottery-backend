package ru.onexteam.lottery.controller;

import io.javalin.Javalin;
import ru.onexteam.lottery.dto.draw.CreateDrawRequest;
import ru.onexteam.lottery.model.Draw;
import ru.onexteam.lottery.service.DrawService;

public class DrawController {

    private static final DrawService drawService = new DrawService();

    public static void register(Javalin app) {

        // USER
        // Получить список тиражей.
        // /api/user/draws?status=active
        app.get("/api/user/draws", ctx -> {
            String status = ctx.queryParam("status");

            if ("active".equals(status)) {
                ctx.json(drawService.getActiveDraws());
            } else {
                ctx.status(400).result("Invalid status");
            }
        });

        // Получить тираж по id.
        app.get("/api/user/draws/{drawId}", ctx -> {
            try {
                long drawId = Long.parseLong(ctx.pathParam("drawId"));

                drawService.getDrawById(drawId)
                        .ifPresentOrElse(
                                ctx::json,
                                () -> ctx.json(404).result("Draw not found")
                        );
            } catch (NumberFormatException ex) {
                ctx.status(400).result("Invalid draw id");
            }
        });

        // Получить результат тиража.
        app.get("/api/draws/{drawId}/result", ctx -> {
            try {
                long drawId = Long.parseLong(ctx.pathParam("drawId"));

                drawService.getDrawById(drawId)
                        .ifPresentOrElse(
                                draw -> {
                                    if ("FINISHED".equals(draw.status)) {
                                        ctx.json(draw);
                                    } else {
                                        ctx.status(400).result("Draw is still active");
                                    }
                                }, () -> ctx.status(404).result("Draw not found")
                        );
            } catch (NumberFormatException ex) {
                ctx.status(400).result("Invalid draw id");
            }
        });

        // ADMIN
        // Создать тираж.
        app.get("/api/admin/draws", ctx -> {
            CreateDrawRequest request = ctx.bodyAsClass(CreateDrawRequest.class);
            Draw draw = drawService.createDraw(request.title);
            ctx.status(201).json(draw);
        });

        // Закрыть тираж.
        app.post("/api/admin/draws/{drawId}/close", ctx -> {
            try {
                long drawId = Long.parseLong(ctx.pathParam("drawId"));

                drawService.getDrawById(drawId)
                        .ifPresentOrElse(draw -> {
                            drawService.finishDraw(draw);
                            ctx.result("Draw closed");
                        }, () -> ctx.status(404).result("Draw not found"));
            } catch (NumberFormatException ex) {
                ctx.status(400).result("Invalid draw id");
            }
        });

    }

}
