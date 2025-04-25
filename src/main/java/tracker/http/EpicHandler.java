package main.java.tracker.http;

import com.sun.net.httpserver.HttpExchange;
import main.java.tracker.controllers.TaskManger;
import main.java.tracker.exceptions.NotFoundException;
import main.java.tracker.model.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {
    private final TaskManger taskManager;

    public EpicHandler(TaskManger taskManager) {
        super();
        this.taskManager = taskManager;
    }

    @Override
    protected void processGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query);
            Epic epic = taskManager.getEpicById(id);
            if (epic != null) {
                sendText(exchange, gson.toJson(epic), 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            List<Epic> epics = taskManager.getEpics();
            sendText(exchange, gson.toJson(epics), 200);
        }
    }

    @Override
    protected void processPost(HttpExchange exchange) throws IOException, NotFoundException {
        InputStream is = exchange.getRequestBody();
        String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(json, Epic.class);
        if (isTimeConflict(epic)) {
            sendHasInteractions(exchange);
            return;
        }
        taskManager.createEpic(epic);
        sendText(exchange, "Задача успешно опубликована", 201);
    }

    @Override
    protected void processDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            taskManager.removeEpic(id);
            sendText(exchange, "Задача успешно удалена", 200);
        } else {
            taskManager.removeAllEpic();
            sendText(exchange, "Все задачи успешно удалены", 200);
        }
    }

    public boolean isTimeConflict(Epic epic) throws NotFoundException {
        return taskManager.taskOverlapWithAnyTask(epic);
    }
}
