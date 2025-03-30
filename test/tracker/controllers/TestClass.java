package tracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.controllers.impl.InMemoryHistoryManager;
import tracker.controllers.impl.InMemoryTaskManager;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.util.Managers;
import tracker.util.Status;

import static org.junit.jupiter.api.Assertions.*;

class TestClass {
    TaskManger taskManager;

    @BeforeEach
    void beforeEach() {
        System.out.println("Before each " + this);
        taskManager = new InMemoryTaskManager();
        taskManager.toZero();
    }

    @Test
    void addNewTaskToCheckTheirId() {
        Task task1 = new Task("Написать тесты", "Определиться что такое тесты", Status.NEW);
        Task task2 = new Task("Понять что такое AssertJ & Hamcrest", "Это библиотека", Status.DONE);
        int taskId = task1.setTaskId(1);
        int taskId2 = task2.setTaskId(1);
        assertEquals(taskManager.getTaskById(taskId), taskManager.getTaskById(taskId2));
    }

    @Test
    void epicIdAndSubtaskId() {
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
        Task task = new Task("Сдать фз сегодня", "Дописать тесты", Status.IN_PROGRESS);
        taskManager.createTask(task);
        int taskId = task.getTaskId();
        Task getTaskId = taskManager.getTaskById(taskId);
        assertEquals(task, getTaskId);
    }

    @Test
    void checkingEverythingIsCorrect() {
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
        Task task = new Task("Приготовить плов", "Купить овощи", Status.NEW);
        task.setTaskId(1);
        taskManager.createTask(task);
        Task task1 = new Task("Стать программистом", "Очень много работать", Status.IN_PROGRESS);
        taskManager.createTask(task1);
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
//    @Test
//    void newTaskCheckingGetTasks() {
//        var get = new InMemoryHistoryManager();
//        Task task1 = new Task("Стать программистом", "Очень много работать", Status.IN_PROGRESS);
//        get.linkLast(task1);
//        List<Task> newGetTask = get.getTasks();
//        assertEquals(1, newGetTask.size());
//        assertEquals(task1, newGetTask.get(0));
//    }
//
//    @Test
//    void newTaskCheckingLinkLast() {
//        var link = new InMemoryHistoryManager();
//        Task task1 = new Task("Стать программистом", "Очень много работать", Status.IN_PROGRESS);
//        link.linkLast(task1);
//    }
//
//    @Test
//    void newTaskCheckingRemove() {
//        var removeTask = new InMemoryHistoryManager();
//        Task task1 = new Task("Стать программистом", "Очень много работать", Status.IN_PROGRESS);
//        removeTask.linkLast(task1);
//        removeTask.remove(0);
//        assertTrue(removeTask.getTasks().isEmpty());
//    }
}