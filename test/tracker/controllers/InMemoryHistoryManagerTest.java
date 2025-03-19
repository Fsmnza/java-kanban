package tracker.controllers;

import org.testng.Assert;
import org.testng.annotations.Test;
import tracker.model.Task;
import tracker.util.Managers;
import tracker.util.Status;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    TaskManger manager = Managers.getDefault();
    @Test
    void getTaskById(){
        Task task1 = new Task("Переезд", "Собирать коробки", Status.NEW);
        manager.createTask(task1);
        Assert.assertEquals(1, task1);
    }
}
