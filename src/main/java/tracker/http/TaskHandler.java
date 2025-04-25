package main.java.tracker.http;

import com.sun.net.httpserver.HttpExchange;
import main.java.tracker.controllers.TaskManger;
import main.java.tracker.exceptions.NotFoundException;
import main.java.tracker.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {

    private final TaskManger taskManager;

    public TaskHandler(TaskManger taskManager) {
        super();
        this.taskManager = taskManager;
    }

    @Override
    protected void processGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query);
            Task task = taskManager.getTaskById(id);
            if (task != null) {
                sendText(exchange, gson.toJson(task), 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            List<Task> tasks = taskManager.getTasks();
            sendText(exchange, gson.toJson(tasks), 200);
        }
    }

    @Override
    protected void processPost(HttpExchange exchange) throws IOException, NotFoundException {
        InputStream is = exchange.getRequestBody();
        String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(json, Task.class);
        if (isTimeConflict(task)) {
            sendHasInteractions(exchange);
            return;
        }
        taskManager.createTask(task);
        sendText(exchange, "Задача успешно опубликована", 201);
    }

    @Override
    protected void processDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            taskManager.removeTask(id);
            sendText(exchange, "Задача успешно удалена", 200);
        } else {
            taskManager.removeAllTasks();
            sendText(exchange, "Все задачи успешно удалены", 200);
        }
    }

    public boolean isTimeConflict(Task task) throws NotFoundException {
        return taskManager.taskOverlapWithAnyTask(task);
    }
}

