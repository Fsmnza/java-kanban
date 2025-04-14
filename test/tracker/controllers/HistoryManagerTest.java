package tracker.controllers;

import main.java.tracker.controllers.impl.InMemoryHistoryManager;
import main.java.tracker.controllers.impl.InMemoryTaskManager;
import main.java.tracker.model.Task;
import main.java.tracker.util.Status;
import main.java.tracker.util.Type;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void emptyLine() {
        assertEquals(Collections.emptyList(), inMemoryHistoryManager.getHistory());
    }

    @Test
    public void dubleTask() {
        Task task = new Task(1, Type.TASK, "Раздать конфеты", Status.NEW, "Купить конфеты",
                LocalDateTime.now());
        taskManager.createTask(task);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        assertEquals(1, taskManager.getHistory().size());
        Task task2 = new Task(2, Type.TASK, "Раздать конфеты", Status.NEW, "Купить конфеты",
                LocalDateTime.now());
        taskManager.createTask(task2);
        taskManager.getTaskById(2);
        assertEquals(2, taskManager.getHistory().size());
    }

    @Test
    public void removeHistory() {
        Task task = new Task(1, Type.TASK, "Раздать конфеты", Status.NEW, "Купить конфеты",
                LocalDateTime.now());
        Task task2 = new Task(2, Type.TASK, "Раздать конфеты", Status.NEW, "Купить конфеты",
                LocalDateTime.now());
        Task task3 = new Task(3, Type.TASK, "Раздать конфеты", Status.NEW, "Купить конфеты",
                LocalDateTime.now());
        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        assertEquals(3, taskManager.getHistory().size());
        taskManager.removeTask(1);
        assertEquals(2, taskManager.getHistory().size());
        assertFalse(taskManager.getHistory().contains(task));
        taskManager.removeTask(2);
        assertEquals(1, taskManager.getHistory().size());
        assertFalse(taskManager.getHistory().contains(task2));
        taskManager.removeTask(3);
        assertEquals(0, taskManager.getHistory().size());
        assertFalse(taskManager.getHistory().contains(task3));
    }

}