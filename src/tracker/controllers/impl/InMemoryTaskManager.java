package tracker.controllers.impl;

import tracker.controllers.HistoryManager;
import tracker.controllers.TaskManger;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.util.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tracker.util.Managers.getDefaultHistory;

public class InMemoryTaskManager implements TaskManger {
    private final Map<Integer, Task> task;
    private final Map<Integer, Subtask> subtask;
    private final Map<Integer, Epic> epic;
    private final HistoryManager historyManager;
    private static int generatorId;

    public InMemoryTaskManager() {
        task = new HashMap<>();
        subtask = new HashMap<>();
        epic = new HashMap<>();
        historyManager = getDefaultHistory();
    }


    public int createTask(Task tasks) {
        final int id = ++generatorId;
        tasks.setTaskId(id);
        task.put(id, tasks);
        return id;
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

    public void removeAllTasks() {
        task.clear();
    }

    public void removeAllSubtasks() {
        for (Epic epics : epic.values()) {
            epics.getSubtaskIds().clear();
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
        Task taskToReturn = task.get(id);
        historyManager.add(taskToReturn);
        return taskToReturn;
    }

    public void removeTask(Integer id) {
        task.remove(id);
    }

    public int createEpic(Epic epics) {
        int id = ++generatorId;
        epics.setTaskId(id);
        epic.put(id, epics);

        return id;
    }

    public int createSubtask(Subtask subtasks) {
        int id = ++generatorId;
        subtasks.setTaskId(id);
        subtask.put(id, subtasks);
        Epic epics = epic.get(subtasks.getEpicID());
        if (epics != null) {
            epics.getSubtaskIds().add(id);
            updateEpicStatus(epics);
        }
        return id;
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
            for (Integer subtaskId : epics.getSubtaskIds()) {
                subtask.remove(subtaskId);
            }
        }
    }

    public Subtask getSubtaskById(int id) {
        Subtask subtaskToReturn = subtask.get(id);
        historyManager.add(subtaskToReturn);
        return subtaskToReturn;
    }

    public Epic getEpicById(int id) {
        Epic epicToReturn = epic.get(id);
        historyManager.add(epicToReturn);
        return epicToReturn;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        List<Integer> epicIds = epic.getSubtaskIds();
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void toZero() {
        generatorId = 0;
    }
}