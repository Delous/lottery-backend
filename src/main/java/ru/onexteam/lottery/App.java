package ru.onexteam.lottery;

import io.javalin.Javalin;
import io.javalin.http.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.onexteam.lottery.config.DbConfig;
import ru.onexteam.lottery.controller.AuthController;
import ru.onexteam.lottery.controller.DocsController;
import ru.onexteam.lottery.controller.DrawController;
import ru.onexteam.lottery.controller.TicketController;
import ru.onexteam.lottery.security.AuthMiddleware;
import ru.onexteam.lottery.security.JwtUtil;
import ru.onexteam.lottery.service.AuthService;

import java.util.concurrent.TimeUnit;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final Logger accessLog = LoggerFactory.getLogger("ru.onexteam.lottery.http.AccessLog");
    private static final String REQUEST_STARTED_AT = "requestStartedAt";

    public static void main(String[] args) {
        DbConfig.initialize();
        log.info("Схема базы данных инициализирована");

        AuthService authService = new AuthService();
        authService.ensureAdmin(DbConfig.getProperty("app.admin.email"), DbConfig.getProperty("app.admin.password"));
        log.info("Default admin account checked");

        Javalin app = Javalin.create();
        configureLogging(app);

        DocsController.register(app);
        AuthController.register(app, authService);

        JwtUtil jwtUtil = new JwtUtil();
        new AuthMiddleware(jwtUtil).register(app);

        DrawController.register(app);
        TicketController.register(app);

        app.start(8080);
        log.info("Сервер запущен: http://localhost:8080");
    }

    private static void configureLogging(Javalin app) {
        app.before(ctx -> ctx.attribute(REQUEST_STARTED_AT, System.nanoTime()));

        app.after(ctx -> {
            Long startedAt = ctx.attribute(REQUEST_STARTED_AT);
            long elapsedMs = startedAt == null
                    ? -1
                    : TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);

            accessLog.info("{} {} -> {} {} ms{}", ctx.method(), ctx.path(), ctx.statusCode(), elapsedMs, errorText(ctx));
        });

        app.exception(HttpResponseException.class, (ex, ctx) ->
                ctx.status(ex.getStatus()).result(ex.getMessage() == null ? "" : ex.getMessage()));

        app.exception(Exception.class, (ex, ctx) -> {
            log.error("Unhandled exception while processing {} {}", ctx.method(), ctx.path(), ex);
            ctx.status(500).result("Internal server error");
        });

        app.error(404, ctx -> ctx.result("Route not found: " + ctx.path()));
    }

    private static String errorText(io.javalin.http.Context ctx) {
        if (ctx.statusCode() < 400) {
            return "";
        }

        String result = ctx.result();
        if (result == null || result.isBlank()) {
            return "";
        }

        return " error=\"" + result.replace("\r", " ").replace("\n", " ").replace("\"", "'") + "\"";
    }
}
