package main.java.tracker.http;

import com.sun.net.httpserver.HttpServer;
import main.java.tracker.controllers.TaskManger;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private HttpServer server;
    private static final int PORT = 8080;

    public HttpTaskServer(TaskManger manager) throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(manager));
        server.createContext("/subtasks", new SubtaskHandler(manager));
        server.createContext("/epics", new EpicHandler(manager));
        server.createContext("/history", new HistoryHandler(manager));
        server.createContext("/prioritized", new PrioritizedHandler(manager));
    }

    public static void main(String[] args) throws IOException {
        System.out.println("HTTP-сервер запущен на порту: " + PORT);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("HTTP сервер остановлен ");
        }
    }


    public void start() {
        server.start();
        System.out.println("HTTP сервер запущен на порту: " + PORT);
    }
}



