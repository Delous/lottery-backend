package ru.onexteam.lottery.controller;

import io.javalin.Javalin;
import ru.onexteam.lottery.dto.ticket.CreateTicketRequest;
import ru.onexteam.lottery.service.TicketService;

public class TicketController {

    private static final TicketService ticketService = new TicketService();

    public static void register(Javalin app) {

        // Получение всех билетов пользователя по id.
        app.get("/api/user/me/tickets", ctx -> {
            // Long userId = ctx.attribute("userId");
            // Из JWT брать id пользователя, который сейчас авторизован.
            // TODO: логика получения билетов для пользователя - ctx.json(ticketService.getUserTickets(userId));
            ctx.status(501).result("Method getUserTickets not implemented");
        });

        // Получение билета по id.
        app.get("/api/user/me/tickets/{ticketId}", ctx -> {
            // Long userId = ctx.attribute("userId");

            try {
                // long ticketId = Long.parseLong(ctx.pathParam("ticketId"));
                // TODO: логика получения билета по id - ctx.json(ticketService.getTicket(userId, ticketId));
                ctx.status(501).result("Method getUserTickets not implemented");
            } catch (NumberFormatException ex) {
                ctx.status(400).result("Invalid ticket id");
            }
        });

        // Получение результата билета по id.
        app.get("/api/user/me/tickets/{ticketId}/result", ctx -> {
            // TODO: логика получения результата для билета по id - ctx.json(ticketService.getTicketResult(userId, ticketId));
            // Long userId = ctx.attribute("userId");

            try {
                long ticketId = Long.parseLong(ctx.pathParam("ticketId"));

                ctx.json(ticketService.getTicketResult(ticketId));
            } catch (NumberFormatException ex) {
                ctx.status(400).result("Invalid ticket id");
            } catch (IllegalArgumentException ex) {
                ctx.status(404).result(ex.getMessage());
            }
        });

        // Купить билет (роль: пользователь).
        app.post("/api/draws/{drawId}/tickets", ctx -> {
            Long userId = ctx.attribute("userId");

            try {
                CreateTicketRequest req =
                        ctx.bodyAsClass(CreateTicketRequest.class);

                if (req.drawId == null) {
                    ctx.status(400).result("drawId is required");
                    return;
                }

                // TODO: так как DTO содержит только drawId, комбинацию передаем временно фиксированную. Пока не изменится DTO.
                String combination = "1,2,3,4,5";

                ctx.status(201).json(
                        ticketService.createTicket(
                                userId,
                                req.drawId,
                                combination
                        )
                );
            } catch (NumberFormatException ex) {
                ctx.status(400).result("Invalid draw id");

            } catch (IllegalArgumentException ex) {
                ctx.status(404).result(ex.getMessage());

            } catch (IllegalStateException ex) {
                ctx.status(409).result(ex.getMessage());
            }
        });
    }

}
