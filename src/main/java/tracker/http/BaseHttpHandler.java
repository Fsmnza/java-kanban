package main.java.tracker.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.tracker.controllers.typeAdapter.DurationAdapter;
import main.java.tracker.controllers.typeAdapter.LocalDateTimeAdapter;
import main.java.tracker.exceptions.NotFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler implements HttpHandler {
    protected static Gson gson = null;

    public BaseHttpHandler() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                processGet(exchange);
                break;
            case "POST":
                try {
                    processPost(exchange);
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "DELETE":
                processDelete(exchange);
                break;
            default:
                writeToUser(exchange, "Данный метод не предусмотрен", 405);
        }
    }

    protected void processGet(HttpExchange exchange) throws IOException {
        writeToUser(exchange, "Метод Get не доступен", 405);
    }

    protected void processPost(HttpExchange exchange) throws IOException, NotFoundException {
        writeToUser(exchange, "Метод Post не доступен", 405);
    }

    protected void processDelete(HttpExchange exchange) throws IOException {
        writeToUser(exchange, "Метод Delete не доступен", 405);
    }

    protected void writeToUser(HttpExchange exchange, String message, int code) throws IOException {
        byte[] resp = message.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendText(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        byte[] resp = "Не найдено".getBytes(StandardCharsets.UTF_8);
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        byte[] resp = "Задача пересекается с уже существующими".getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public static Gson getGson() {
        return gson;
    }
}
