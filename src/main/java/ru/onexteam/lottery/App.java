package ru.onexteam.lottery;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.onexteam.lottery.controller.AuthController;
import ru.onexteam.lottery.controller.DrawController;
import ru.onexteam.lottery.controller.TicketController;
import ru.onexteam.lottery.dto.error.ErrorResponse;
import ru.onexteam.lottery.service.AuthService;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        Javalin app = Javalin.create();

        registerRoutes(app);
        registerExceptionHandler(app);

        app.start(7000);
    }

    private static void registerRoutes(Javalin app) {
        AuthController.register(app, new AuthService());
        DrawController.register(app);
        TicketController.register(app);
    }

    private static void registerExceptionHandler(Javalin app) {

        app.exception(Exception.class, (e, ctx) -> {
            log.error("{}", e.getMessage(), e);
            ctx.status(500).json(new ErrorResponse("Internal server error"));
        });

    }

}
