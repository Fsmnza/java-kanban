package main.java.tracker.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import main.java.tracker.controllers.TaskManger;
import main.java.tracker.controllers.typeAdapter.DurationAdapter;
import main.java.tracker.controllers.typeAdapter.LocalDateTimeAdapter;
import main.java.tracker.util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static HttpServer server;
    private static TaskManger manager;
    private static final int PORT = 8080;
    private static Gson gson;

    public static void main(String[] args) throws IOException {
        manager = Managers.getDefault();
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(manager));
        server.createContext("/subtasks", new SubtaskHandler(manager));
        server.createContext("/epics", new EpicHandler(manager));
        server.createContext("/history", new HistoryHandler(manager));
        server.createContext("/prioritized", new PrioritizedHandler(manager));
        server.start();
        System.out.println("HTTP server started on port " + PORT);
    }

    public static void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("HTTP server stopped.");
        }
    }
}



