package tracker.controllers;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.ArrayList;

public interface TaskManger {

    int createTask(Task tasks);

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    void removeAllTAsk();

    void removeAllSubtask();

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

}
