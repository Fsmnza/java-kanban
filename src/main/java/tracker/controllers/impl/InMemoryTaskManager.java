package main.java.tracker.controllers.impl;

import main.java.tracker.controllers.HistoryManager;
import main.java.tracker.controllers.TaskManger;
import main.java.tracker.model.Epic;
import main.java.tracker.model.Subtask;
import main.java.tracker.model.Task;
import main.java.tracker.util.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static main.java.tracker.util.Managers.getDefaultHistory;

public class InMemoryTaskManager implements TaskManger {
    public final Map<Integer, Task> task;
    final Map<Integer, Subtask> subtask;
    final Map<Integer, Epic> epic;
    private final HistoryManager historyManager;
    private int generatorId;

    public InMemoryTaskManager() {
        task = new HashMap<>();
        subtask = new HashMap<>();
        epic = new HashMap<>();
        historyManager = getDefaultHistory();
    }


    public void updateEpicTimeAndDuration(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(Duration.ZERO);
            return;
        }
        LocalDateTime newStartTime = null;
        LocalDateTime newEndTime = null;
        Duration newDuration = Duration.ZERO;

        for (Integer subtaskId : subtaskIds) {
            Subtask sub = subtask.get(subtaskId);
            if (sub != null && sub.getStartTime() != null && sub.getDuration() != null) {
                if (newStartTime == null || sub.getStartTime().isBefore(newStartTime)) {
                    newStartTime = sub.getStartTime();
                }
                LocalDateTime subEndTime = sub.getStartTime().plus(sub.getDuration());
                if (newEndTime == null || subEndTime.isAfter(newEndTime)) {
                    newEndTime = subEndTime;
                }
                newDuration = newDuration.plus(sub.getDuration());
            }
        }
        epic.setStartTime(newStartTime);
        epic.setEndTime(newEndTime);
        epic.setDuration(newDuration);
    }

    public TreeSet<Task> getPrioritizedTasks(Epic epics, Task tasks, Subtask subtasks) {
        TreeSet<Task> treeSet = new TreeSet<>();
        task.values().stream()
                .filter(task -> task.getStartTime() != null)
                .forEach(treeSet::add);
        epic.values().stream()
                .filter(epic -> epic.getStartTime() != null)
                .forEach(treeSet::add);
        subtask.values().stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .forEach(subtask -> treeSet.add(subtask));
        return treeSet;
    }

    public int createTask(Task tasks) {
        final int id = ++generatorId;
        tasks.setTaskId(id);
        task.put(id, tasks);
        return id;
    }

    public static boolean checkingIntersections(Task task1, Task task2) {
        if (task2.getStartTime() != null || task1.getStartTime() != null) {
            LocalDateTime startTime = task2.getStartTime();
            LocalDateTime startTime2 = task1.getStartTime();
            LocalDateTime endTime = task2.getEndTime();
            LocalDateTime endTime2 = task1.getEndTime();
            return startTime2.isBefore(endTime) && startTime.isBefore(endTime2);
        }
        return false;
    }

    public boolean taskOverlapWithAnyTask(Task newTask) {
        List<Task> allTaskList = new ArrayList<>();
        allTaskList.addAll(task.values());
        allTaskList.addAll(subtask.values());
        return allTaskList.stream()
                .filter(task1 -> !task1.equals(newTask))
                .anyMatch(allTaskLists -> checkingIntersections(allTaskLists, newTask));
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
        for (int id : epic.keySet()) {
            historyManager.remove(id);
        }
        task.clear();
    }

    public void removeAllSubtasks() {
        for (int id : epic.keySet()) {
            historyManager.remove(id);
        }
        for (Epic epics : epic.values()) {
            epics.getSubtaskIds().clear();
            updateEpicStatus(epics);
        }
        subtask.clear();
    }

    public void removeAllEpic() {
        for (int id : epic.keySet()) {
            historyManager.remove(id);
        }
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
        historyManager.remove(id);
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
            updateEpicTimeAndDuration(epics);
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
            updateEpicTimeAndDuration(epics);
        }
        return subtasks;
    }

    public void removeSubtask(int id) {
        Subtask subtasks = subtask.remove(id);
        if (subtasks != null) {
            Epic epics = epic.get(subtasks.getEpicID());
            if (epics != null) {
                epics.getSubtaskIds().remove((Integer) id);
                updateEpicStatus(epics);
                updateEpicTimeAndDuration(epics);
            }
        }
        historyManager.remove(id);
    }

    public void removeEpic(int id) {
        final Epic epics = epic.remove(id);
        if (epics != null) {
            for (Integer subtaskId : epics.getSubtaskIds()) {
                subtask.remove(subtaskId);
            }
        }
        historyManager.remove(id);
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
        boolean allNew = epicIds.stream()
                .map(id -> subtask.get(id))
                .anyMatch(subtask1 -> subtask1.getStatus() == Status.NEW);
        boolean allDone = epicIds.stream()
                .map(id -> subtask.get(id))
                .allMatch(subtask1 -> subtask1.getStatus() != Status.DONE);
        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public void setNextTaskId(int nextId) {
        this.generatorId = nextId;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}