package main.java.tracker.controllers;

import main.java.tracker.model.Epic;
import main.java.tracker.model.Subtask;
import main.java.tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManger {

    List<Task> getPrioritizedTasks();

    int createTask(Task tasks);

    boolean taskOverlapWithAnyTask(Task newTask);

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    void removeAllTasks();

    void removeAllSubtasks();

    void removeAllEpic();

    Task updateTask(Task tasks);

    Task getTaskById(Integer id);

    void removeTask(Integer id);

    int createEpic(Epic epics);

    int createSubtask(Subtask subtasks);

    Epic updateEpic(Epic epics);

    Subtask updateSubtask(Subtask subtasks);

    void removeSubtask(int id);

    void removeEpic(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    void updateEpicStatus(Epic epic);

    List<Task> getHistory();

}
