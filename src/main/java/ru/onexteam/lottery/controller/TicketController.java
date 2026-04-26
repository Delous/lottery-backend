package ru.onexteam.lottery.controller;

import io.javalin.Javalin;
import ru.onexteam.lottery.dto.ticket.CreateTicketRequest;
import ru.onexteam.lottery.service.TicketService;

public class TicketController {

    private static final TicketService ticketService = new TicketService();

    public static void register(Javalin app) {

        app.post("/api/draws/{drawId}/tickets", ctx -> {
            // TODO: AuthMiddleware проверка роли (требуемое значение "user").
            Long userId = ctx.attribute("userId");
            CreateTicketRequest req = ctx.bodyAsClass(CreateTicketRequest.class);
            String result = ticketService.createTicket(userId, req.drawId);
            ctx.status(201).json(result);
        });

    }

}
