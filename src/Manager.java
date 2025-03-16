import java.util.*;

public class Manager {
    private final HashMap<Integer, Task> task;
    private final HashMap<Integer, Subtask> subtask;
    private final HashMap<Integer, Epic> epic;
    private static int id = 0;

    public Manager(HashMap<Integer, Task> task, HashMap<Integer, Subtask> subtask, HashMap<Integer, Epic> epic) {
        this.task = task;
        this.subtask = subtask;
        this.epic = epic;
    }

    public static int idCounter() {
        return id++;
    }

    public void createTask(Task tasks) {
        task.put(tasks.getTaskId(), tasks);
    }

    public Map<Integer, Task> getAllTask() {
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(task);
        allTasks.putAll(subtask);
        allTasks.putAll(epic);
        return allTasks;
    }

    public void removeAll() {
        task.clear();
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
        epic.put(epics.getTaskId(), epics);
    }

    public void createSubtask(Subtask subtasks) {
        subtask.put(subtasks.getTaskId(), subtasks);
        Epic epics = epic.get(subtasks.getSubtaskId());
        if (epics != null) {
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
        Epic epics = epic.get(subtasks.getSubtaskId());
        if (epics != null) {
            updateEpicStatus(epics);
        }
        return subtasks;
    }

    public void removeSubtask(int id) {
        subtask.remove(id);

    }

    public void removeEpic(int id) {
        epic.remove(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtask.get(id);
    }

    public Epic getEpicById(int id) {
        return epic.get(id);
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> epicIds = epic.getEpicId();
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