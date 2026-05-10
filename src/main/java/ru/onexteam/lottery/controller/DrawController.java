package ru.onexteam.lottery.controller;

import io.javalin.Javalin;
import ru.onexteam.lottery.dto.draw.CreateDrawRequest;
import ru.onexteam.lottery.model.Draw;
import ru.onexteam.lottery.service.DrawService;
import ru.onexteam.lottery.service.LotteryService;

public class DrawController {

    private static final DrawService drawService = new DrawService();
    private static final LotteryService lotteryService = new LotteryService();

    public static void register(Javalin app) {
        app.get("/api/user/draws", ctx -> {
            String status = ctx.queryParam("status");

            if (status == null || status.isBlank() || "active".equalsIgnoreCase(status)) {
                ctx.json(drawService.getActiveDraws());
                return;
            }

            ctx.status(400).result("Недопустимый статус");
        });

        app.get("/api/user/draws/{drawId}", ctx -> {
            try {
                long drawId = Long.parseLong(ctx.pathParam("drawId"));
                drawService.getDrawById(drawId)
                        .ifPresentOrElse(ctx::json, () -> ctx.status(404).result("Тираж не найден"));
            } catch (NumberFormatException ex) {
                ctx.status(400).result("Недопустимый идентификатор тиража");
            }
        });

        app.get("/api/user/draws/{drawId}/result", ctx -> {
            try {
                long drawId = Long.parseLong(ctx.pathParam("drawId"));
                drawService.getDrawResult(drawId)
                        .ifPresentOrElse(ctx::json, () -> ctx.status(404).result("Результат тиража не найден"));
            } catch (NumberFormatException ex) {
                ctx.status(400).result("Недопустимый идентификатор тиража");
            }
        });

        app.post("/api/admin/draws", ctx -> {
            try {
                CreateDrawRequest request = ctx.bodyAsClass(CreateDrawRequest.class);
                Draw draw = drawService.createDraw(request.title);
                ctx.status(201).json(draw);
            } catch (IllegalArgumentException ex) {
                ctx.status(400).result(ex.getMessage());
            }
        });

        app.post("/api/admin/draws/{drawId}/close", ctx -> {
            try {
                long drawId = Long.parseLong(ctx.pathParam("drawId"));
                ctx.json(lotteryService.runDraw(drawId));
            } catch (NumberFormatException ex) {
                ctx.status(400).result("Недопустимый идентификатор тиража");
            } catch (IllegalArgumentException ex) {
                ctx.status(404).result(ex.getMessage());
            } catch (IllegalStateException ex) {
                ctx.status(409).result(ex.getMessage());
            }
        });
    }
}
