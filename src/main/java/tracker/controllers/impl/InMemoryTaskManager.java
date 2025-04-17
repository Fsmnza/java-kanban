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
    private final TreeSet<Task> prioritizedTasks;
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
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())));
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


    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }


    public int createTask(Task tasks) {
        if (taskOverlapWithAnyTask(tasks)) {
            throw new IllegalArgumentException("Задача пересекается с другой задачей по врмеени");
        }
        final int id = ++generatorId;
        tasks.setTaskId(id);
        task.put(id, tasks);
        if (tasks.getStartTime() != null) {
            prioritizedTasks.add(tasks);
        }
        return id;
    }

    private boolean checkingIntersections(Task newTask, Task existingTask) {
        if (newTask.getStartTime() == null || newTask.getEndTime() == null ||
                existingTask.getStartTime() == null || existingTask.getEndTime() == null) {
            return false;
        }
        return newTask.getStartTime().isBefore(existingTask.getEndTime()) &&
                newTask.getEndTime().isAfter(existingTask.getStartTime());
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
            prioritizedTasks.remove(task.get(id));
        }
        task.clear();
    }

    public void removeAllSubtasks() {
        for (int id : epic.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(task.get(id));
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
            prioritizedTasks.remove(task.get(id));
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
        Task task1 = task.remove(id);
        if (task1 != null && task1.getStartTime() != null) {
            prioritizedTasks.remove(task1);
        }
        historyManager.remove(id);
    }

    public int createEpic(Epic epics) {
        if (taskOverlapWithAnyTask(epics)) {
            throw new IllegalArgumentException("Задача пересекается с другой задачей по врмеени");
        }
        int id = ++generatorId;
        epics.setTaskId(id);
        epic.put(id, epics);
        if (epics.getStartTime() != null) {
            prioritizedTasks.add(epics);
        }
        return id;
    }

    public int createSubtask(Subtask subtasks) {
        if (taskOverlapWithAnyTask(subtasks)) {
            throw new IllegalArgumentException("Задача пересекается с другой задачей по врмеени");
        }
        int id = ++generatorId;
        subtasks.setTaskId(id);
        subtask.put(id, subtasks);
        Epic epics = epic.get(subtasks.getEpicID());
        if (epics != null) {
            epics.getSubtaskIds().add(id);
            updateEpicStatus(epics);
            updateEpicTimeAndDuration(epics);
        }
        if (subtasks.getStartTime() != null) {
            prioritizedTasks.add(subtasks);
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
        if (subtasks != null && subtasks.getStartTime() != null) {
            Epic epics = epic.get(subtasks.getEpicID());
            if (epics != null) {
                epics.getSubtaskIds().remove((Integer) id);
                updateEpicStatus(epics);
                updateEpicTimeAndDuration(epics);
                prioritizedTasks.remove(subtasks);
            }
        }
        historyManager.remove(id);
    }

    public void removeEpic(int id) {
        final Epic epics = epic.remove(id);
        if (epics != null && epics.getStartTime() != null) {
            for (Integer subtaskId : epics.getSubtaskIds()) {
                subtask.remove(subtaskId);
                prioritizedTasks.remove(epics);
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