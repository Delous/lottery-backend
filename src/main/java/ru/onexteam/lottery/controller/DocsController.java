package ru.onexteam.lottery.controller;

import io.javalin.Javalin;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class DocsController {

    private static final String OPENAPI_JSON = readResource("openapi.json");
    private static final String DOCS_HTML = readResource("docs.html");

    public static void register(Javalin app) {
        app.get("/openapi.json", ctx -> ctx.contentType("application/json").result(OPENAPI_JSON));
        app.get("/docs", ctx -> ctx.contentType("text/html").result(DOCS_HTML));
    }

    private static String readResource(String name) {
        try (InputStream inputStream = DocsController.class.getClassLoader().getResourceAsStream(name)) {
            if (inputStream == null) {
                throw new IllegalStateException("Resource not found: " + name);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read resource: " + name, e);
        }
    }
}
