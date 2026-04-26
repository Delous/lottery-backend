package ru.onexteam.lottery.controller;

import io.javalin.Javalin;
import ru.onexteam.lottery.dto.draw.CreateDrawRequest;
import ru.onexteam.lottery.service.DrawService;

public class DrawController {

    private static final DrawService drawService = new DrawService();

    public static void register(Javalin app) {

        // Создать тираж (роль: админ).
        app.post("/api/draws", ctx -> {
            // TODO: логика создания тиража админом.
        });

        // Закрыть тираж (роль: админ).
        app.post("/api/draws/{drawId}/close", ctx -> {
            long drawId = Long.parseLong(ctx.pathParam("drawId")); // Может кинуть NumberFormatException.

            // TODO: при закрытии тиража получение билетов становится невозможным и сразу определяется победная комбинация.
        });

        // Получить список тиражей.
        // /api/draws?status=active
        app.get("/api/draws", ctx -> {
            String status = ctx.queryParam("status");

            if ("active".equals(status)) {
                ctx.json(drawService.getActiveDraws());
                return;
            }

            // TODO: логика получения всех тиражей - ctx.json(drawService.getAllDraws());
        });

        // Получить тираж по id.
        app.get("/api/draws/{drawId}", ctx -> {
            long drawId = Long.parseLong(ctx.pathParam("drawId")); // Может кинуть NumberFormatException.

            // TODO: логика получения тиража по id - ctx.json(drawService.getById(drawId));
        });

        // Получить результат тиража.
        app.get("/api/draws/{drawId}/result", ctx -> {
            long drawId = Long.parseLong(ctx.pathParam("drawId")); // Может кинуть NumberFormatException.

            // TODO: логика получения результата для тиража по его id - ctx.json(drawService.getResult(drawId));
        });

    }

}
