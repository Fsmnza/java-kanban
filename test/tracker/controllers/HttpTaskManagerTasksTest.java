package tracker.controllers;

import com.google.gson.Gson;
import main.java.tracker.controllers.TaskManger;
import main.java.tracker.controllers.impl.InMemoryTaskManager;
import main.java.tracker.http.BaseHttpHandler;
import main.java.tracker.http.HttpTaskServer;
import main.java.tracker.model.Task;
import main.java.tracker.util.Status;
import main.java.tracker.util.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {

    private TaskManger manager;
    private HttpTaskServer taskServer;
    private Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        gson = BaseHttpHandler.getGson();
        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpic();
        taskServer.start();
    }

    @AfterEach
    void shutDown() {
        taskServer.stop();
    }

    @Test
    void testAddTask() throws IOException, InterruptedException {
        Task task = new Task(1, Type.TASK, "Сьездить на море", Status.NEW, "Попросить отпуск",
                LocalDateTime.now());

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Ответ от сервера должен быть 200");
        List<Task> tasksFromManager = manager.getTasks();
        assertNotNull(tasksFromManager, "Список задач не должен быть пустым");
        assertEquals(1, tasksFromManager.size(), "Должна быть создана одна задача");
        assertEquals("Сьездить на море", tasksFromManager.get(0).getName(), "Имя задачи некорректное");
    }

    @Test
    void testAddInvalidTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertTrue(response.statusCode() == 400 || response.statusCode() == 405,
                "Неверный запрос должен вернуть 400 или 405");
    }
}
