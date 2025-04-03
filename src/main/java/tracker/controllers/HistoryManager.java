package main.java.tracker.controllers;

import main.java.tracker.model.Task;

import java.util.List;

public interface HistoryManager {
    boolean add(Task task);

    List<Task> getHistory();

    void remove(int id);
}