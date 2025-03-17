package tracker.controllers;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.util.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private final HashMap<Integer, Task> task;
    private final HashMap<Integer, Subtask> subtask;
    private final HashMap<Integer, Epic> epic;
    private static int generatorId;

    public Manager() {
        task = new HashMap<>();
        subtask = new HashMap<>();
        epic = new HashMap<>();
    }

    public void createTask(Task tasks) {
        final int id = ++generatorId;
        tasks.setTaskId(id);
        task.put(id, tasks);
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(task.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtask.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epic.values());
    }

    public void removeAllTAsk() {
        task.clear();
    }

    public void removeAllSubtask() {
        for (Epic epics : epic.values()) {
            epics.getSubtaskId().clear();
            updateEpicStatus(epics);
        }
        subtask.clear();
    }

    public void removeAllEpic() {
        epic.clear();
        subtask.clear();
    }

    public Task updateTask(Task tasks) {
        if (task.containsKey(tasks.getTaskId())) {
            task.put(tasks.getTaskId(), tasks);
        }
        return tasks;
    }

    public Task getTaskById(Integer id) {
        return task.get(id);
    }

    public void removeTask(Integer id) {
        task.remove(id);
    }

    public void createEpic(Epic epics) {
        int id = ++generatorId;
        epics.setTaskId(id);
        epic.put(id, epics);

    }

    public void createSubtask(Subtask subtasks) {
        final int id = ++generatorId;
        subtasks.setTaskId(id);
        subtask.put(id, subtasks);
        Epic epics = epic.get(subtasks.getEpicID());
        if (epics != null) {
            epics.addSubtaskId(id);
            updateEpicStatus(epics);
        }
    }

    public Epic updateEpic(Epic epics) {
        if (epic.containsKey(epics.getTaskId())) {
            epic.put(epics.getTaskId(), epics);
        }
        return epics;
    }

    public Subtask updateSubtask(Subtask subtasks) {
        if (subtask.containsKey(subtasks.getTaskId())) {
            subtask.put(subtasks.getTaskId(), subtasks);
        }
        Epic epics = epic.get(subtasks.getEpicID());
        if (epics != null) {
            updateEpicStatus(epics);
        }
        return subtasks;
    }

    public void removeSubtask(int id) {
        Subtask subtasks = subtask.remove(id);
        if (subtasks != null) {
            Epic epics = epic.get(subtasks.getEpicID());
            if (epics != null) {
                epic.get(id);
                updateEpicStatus(epics);
            }
        }
    }

    public void removeEpic(int id) {
        final Epic epics = epic.remove(id);
        if (epics != null) {
            for (Integer subtaskId : epics.getSubtaskId()) {
                subtask.remove(subtaskId);
            }
        }
    }

    public Subtask getSubtaskById(int id) {
        return subtask.get(id);
    }

    public Epic getEpicById(int id) {
        return epic.get(id);
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> epicIds = epic.getSubtaskId();
        if (epicIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        boolean allNew = true;
        boolean allDone = true;
        for (int id : epicIds) {
            Subtask subtasks = subtask.get(id);
            if (subtasks.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtasks.getStatus() != Status.DONE) {
                allDone = false;
            }
        }
        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}