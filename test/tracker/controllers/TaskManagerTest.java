package tracker.controllers;

import main.java.tracker.controllers.TaskManger;
import main.java.tracker.model.Task;
import main.java.tracker.util.Status;
import main.java.tracker.util.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public abstract class TaskManagerTest<T extends TaskManger> {
    protected T manager;

    protected abstract T createManager();

    LocalDateTime newTime = LocalDateTime.of(2025, 4, 11, 10, 36);

    @BeforeEach
    public void setUp() {
        manager = createManager();
    }

    @Test
    public void createNewTask() {
        Task task = new Task(1, Type.TASK, "Раздать конфеты", Status.NEW, "Купить конфеты",
                newTime);
        manager.createTask(task);
        Task getId = manager.getTaskById(task.getTaskId());
        assertEquals(task, getId);
    }

    @Test
    public void updateNewTask() {
        Task task = new Task(1, Type.TASK, "Раздать конфеты", Status.NEW, "Купить конфеты",
                newTime);
        manager.createTask(task);
        task.setStatus(Status.DONE);
        manager.updateTask(task);
        assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    public void deleteNewTask() {
        Task task = new Task(1, Type.TASK, "Раздать конфеты", Status.NEW, "Купить конфеты",
                newTime);
        manager.createTask(task);
        manager.removeTask(1);
        assertNull(manager.getTaskById(1));
    }
}

