package ru.onexteam.lottery.controller;

import io.javalin.Javalin;
import ru.onexteam.lottery.dto.ticket.CreateTicketRequest;
import ru.onexteam.lottery.service.TicketService;

public class TicketController {

    private static final TicketService ticketService = new TicketService();

    public static void register(Javalin app) {

        // Купить билет (роль: пользователь).
        app.post("/api/draws/{drawId}/tickets", ctx -> {
            long drawId = Long.parseLong(ctx.pathParam("drawId")); // Может выкинуть NumberFormatException.

            // Из JWT брать id пользователя.

            // TODO: логика приобретения (создания) билета - ctx.json(ticketService.createTicket(userId, drawId, req));.
        });

        // Получение всех билетов пользователя по id.
        app.get("/api/me/tickets", ctx -> {
            // Из JWT брать id пользователя, который сейчас авторизован.
            // TODO: логика получения билетов для пользователя - ctx.json(ticketService.getUserTickets(userId));
        });

        // Получение билета по id.
        app.get("/api/me/tickets/{ticketId}", ctx -> {
            // TODO: логика получения билета по id - ctx.json(ticketService.getTicket(userId, ticketId));
        });

        // Получение результата билета по id.
        app.get("/api/me/tickets/{ticketId}/result", ctx -> {
            // TODO: логика получения результата для билета по id - ctx.json(ticketService.getTicketResult(userId, ticketId));
        });

    }

}
