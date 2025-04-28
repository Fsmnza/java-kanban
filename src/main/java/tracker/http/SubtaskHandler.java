package main.java.tracker.http;

import com.sun.net.httpserver.HttpExchange;
import main.java.tracker.controllers.TaskManger;
import main.java.tracker.exceptions.NotFoundException;
import main.java.tracker.model.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {

    private final TaskManger taskManager;

    public SubtaskHandler(TaskManger taskManager) {
        super();
        this.taskManager = taskManager;
    }

    @Override
    protected void processGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query);
            Subtask subtask = taskManager.getSubtaskById(id);
            if (subtask != null) {
                sendText(exchange, gson.toJson(subtask), 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            List<Subtask> subtasks = taskManager.getSubtasks();
            sendText(exchange, gson.toJson(subtasks), 200);
        }
    }

    @Override
    protected void processPost(HttpExchange exchange) throws IOException, NotFoundException {
        InputStream is = exchange.getRequestBody();
        String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        if (json.isBlank()) {
            writeToUser(exchange, "Тело запроса пустое", 400);
            return;
        }
        Subtask subtask = gson.fromJson(json, Subtask.class);
        if (subtask == null) {
            writeToUser(exchange, "Некорректный формат задачи", 400);
            return;
        }
        if (isTimeConflict(subtask)) {
            sendHasInteractions(exchange);
            return;
        }
        taskManager.createSubtask(subtask);
        sendText(exchange, "Задача успешно опубликована", 201);
    }

    @Override
    protected void processDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            taskManager.removeSubtask(id);
            sendText(exchange, "Задача успешно удалена", 200);
        } else {
            taskManager.removeAllSubtasks();
            sendText(exchange, "Все задачи успешно удалены", 200);
        }
    }

    public boolean isTimeConflict(Subtask subtask) throws NotFoundException {
        return taskManager.taskOverlapWithAnyTask(subtask);
    }
}
