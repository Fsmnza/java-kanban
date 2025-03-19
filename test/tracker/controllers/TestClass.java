package tracker.controllers;

import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.util.Managers;
import tracker.util.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestClass {
    @Test
    void addNewTaskToCheckTheirId() {
        var taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Написать тесты", "Определиться что такое тесты", Status.NEW);
        Task task2 = new Task("Понять что такое AssertJ & Hamcrest", "Это библиотека", Status.DONE);
        int taskId = task1.setTaskId(1);
        int taskId2 = task2.setTaskId(1);
        assertEquals(taskManager.getTaskById(taskId), taskManager.getTaskById(taskId2));
    }

    @Test
    void epicIdAndSubtaskId() {
        var taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Сходить за хлебом", "Одеться", Status.NEW);
        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description", Status.NEW, epic.getTaskId());
        int epicId = epic.setTaskId(1);
        int subtaskId = subtask.setTaskId(1);
        assertEquals(taskManager.getSubtaskById(subtaskId), taskManager.getEpicById(epicId));
    }

    @Test
    void epicCanNotBeHisEpic() {
        Epic epic = new Epic("Сходить за хлебом", "Одеться", Status.NEW);
        epic.setTaskId(1);
        Epic epic2 = new Epic("Увидеть цветение сакуры", "Купить билеты в ЯПонию", Status.NEW);
        epic2.setTaskId(epic.getTaskId());
        assertEquals(epic.getTaskId(), epic2.getTaskId());
    }

    @Test
    void subtaskIsNotHisEpic() {
        Subtask subtask2 = new Subtask("Увидеть цветение сакуры", "Купить билеты в Японию", Status.NEW, 5);
        subtask2.setTaskId(6);
        assertNotEquals(subtask2.getTaskId(), subtask2.getEpicID());
    }


    @Test
    void checkingUtilityClass() {
        var taskManager = Managers.getDefault();
        var historyManager = Managers.getDefaultHistory();
        assertInstanceOf(InMemoryTaskManager.class, taskManager);
        assertInstanceOf(InMemoryHistoryManager.class, historyManager);
    }

    @Test
    void addTaskDifferentTypeAndDetId() {
        var taskManager = new InMemoryTaskManager();
        Task task = new Task("Сдать фз сегодня", "Дописать тесты", Status.IN_PROGRESS);
        taskManager.createTask(task);
        int taskId = task.getTaskId();
        Task getTaskId = taskManager.getTaskById(taskId);
        assertEquals(task, getTaskId);
    }

    @Test
    void checkingEverythingIsCorrect() {
        var taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Сходить за хлебом", "Одеться", Status.NEW);
        int epicId = taskManager.createEpic(epic1);
        Task task1 = new Task("Купить корм", "Сьездть в зоо магазин", Status.IN_PROGRESS);
        int taskId = taskManager.createTask(task1);
        Subtask subtask1 = new Subtask("Сходить за хлебом", "Купить в магазине", Status.DONE, epic1.getTaskId());
        int subtaskId = taskManager.createSubtask(subtask1);
        assertEquals(epic1, taskManager.getEpicById(epicId));
        assertEquals(task1, taskManager.getTaskById(taskId));
        assertEquals(subtask1, taskManager.getSubtaskById(subtaskId));
    }

    @Test
    void tasksWithManualAndGeneratedIdDoNotConflict() {
        TaskManger manager = new InMemoryTaskManager();
        Task task = new Task("Приготовить плов", "Купить овощи", Status.NEW);
        task.setTaskId(1);
        manager.createTask(task);
        Task task1 = new Task("Стать программистом", "Очень много работать", Status.IN_PROGRESS);
        manager.createTask(task1);
        assertNotEquals(task.getTaskId(), task1.getTaskId());
    }

    @Test
    void nothingChangeAfterAddingInTaskManager() {
        var manager = new InMemoryTaskManager();
        Task task1 = new Task("Стать программистом", "Очень много работать", Status.IN_PROGRESS);
        manager.createTask(task1);
        Task taskId = manager.getTaskById(1);
        assertEquals(task1.getName(), taskId.getName());
        assertEquals(task1.getDescription(), taskId.getDescription());
        assertEquals(task1.getStatus(), taskId.getStatus());

    }

//    @AfterEach
//    void afterEach() {
//        System.out.println("After each "+this);
//    }

    @Test
    void newTaskDidntChangeAfterNewData() {
        var manager = new InMemoryTaskManager();
        var historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Стать программистом", "Очень много работать", Status.IN_PROGRESS);
        manager.createTask(task1);
        Task taskId = manager.getTaskById(1);
        Task task2 = new Task(task1.getName(), task1.getDescription(), task1.getStatus());
        task2.setTaskId(task1.getTaskId());
        historyManager.add(task2);
        taskId.setStatus(Status.DONE);
        List<Task> history = historyManager.getHistory();
        assertEquals(Status.IN_PROGRESS, history.get(0).getStatus()); //когда совместно идет тест падает, а отдельно все ок
    }
}
