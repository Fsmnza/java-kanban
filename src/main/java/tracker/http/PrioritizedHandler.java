package main.java.tracker.http;

import com.sun.net.httpserver.HttpExchange;
import main.java.tracker.controllers.TaskManger;
import main.java.tracker.model.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {
    private final TaskManger taskManager;

    public PrioritizedHandler(TaskManger taskManager) {
        super();
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            List<Task> tasks = new ArrayList<>(taskManager.getPrioritizedTasks());
            String response = gson.toJson(tasks);
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        } else {
            writeToUser(exchange, "Данный метод не предусмотрен", 405);
        }
    }
}
