package ru.onexteam.lottery.controller;

import io.javalin.Javalin;
import ru.onexteam.lottery.service.TicketService;

public class TicketController {

    private static final TicketService ticketService = new TicketService();

    public static void register(Javalin app) {
        app.get("/api/user/me/tickets", ctx -> {
            Long userId = ctx.attribute("userId");
            ctx.json(ticketService.getUserTickets(userId));
        });

        app.get("/api/user/me/tickets/{ticketId}", ctx -> {
            Long userId = ctx.attribute("userId");

            try {
                long ticketId = Long.parseLong(ctx.pathParam("ticketId"));
                ctx.json(ticketService.getUserTicket(userId, ticketId));
            } catch (NumberFormatException ex) {
                ctx.status(400).result("Недопустимый идентификатор билета");
            } catch (IllegalArgumentException ex) {
                ctx.status(404).result(ex.getMessage());
            }
        });

        app.get("/api/user/me/tickets/{ticketId}/result", ctx -> {
            Long userId = ctx.attribute("userId");

            try {
                long ticketId = Long.parseLong(ctx.pathParam("ticketId"));
                ctx.json(ticketService.getTicketResult(userId, ticketId));
            } catch (NumberFormatException ex) {
                ctx.status(400).result("Недопустимый идентификатор билета");
            } catch (IllegalArgumentException ex) {
                ctx.status(404).result(ex.getMessage());
            } catch (IllegalStateException ex) {
                ctx.status(409).result(ex.getMessage());
            }
        });

        app.post("/api/user/draws/{drawId}/tickets", ctx -> {
            Long userId = ctx.attribute("userId");

            try {
                long drawId = Long.parseLong(ctx.pathParam("drawId"));
                ctx.status(201).json(ticketService.createTicket(userId, drawId));
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
