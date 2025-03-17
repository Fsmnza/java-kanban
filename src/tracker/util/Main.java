package tracker.util;

import tracker.controllers.Manager;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Переезд", "Собирать коробки", Status.NEW);
        Task task2 = new Task("Купить хлеб", "Сходить в магазин", Status.IN_PROGRESS);
        manager.createTask(task1);
        manager.createTask(task2);
        Epic epic1 = new Epic("Переезд", "Подготовка к переезду", Status.NEW);
        manager.createEpic(epic1);
        System.out.println("Эпик без подзадач: " + manager.getEpicById(epic1.getTaskId()));
        Subtask sub3 = new Subtask("Собрать вещи", "Собрать все в коробки", Status.IN_PROGRESS, epic1.getTaskId());
        manager.createSubtask(sub3);
        System.out.println("Проверка " + manager.getTasks());
        Subtask updateSub = new Subtask("Забрать документы", "ьездить в университет", Status.DONE, epic1.getTaskId());
        manager.removeSubtask(3);
        System.out.println(manager.updateSubtask(updateSub));
        System.out.println("Проверка2 " + manager.getSubtasks());
        System.out.println("Проверка3 " + manager.getEpics());
        manager.createTask(task1);
        manager.createTask(task2);
        Subtask sub1 = new Subtask("Собрать вещи", "Собрать все в коробки", Status.NEW, epic1.getTaskId());
        Subtask sub2 = new Subtask("Упаковать технику", "Осторожно сложить технику", Status.NEW, epic1.getTaskId());
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);
        System.out.println("Эпик c подзадачами: " + manager.getEpicById(epic1.getTaskId()));
        System.out.println("Все задачи: " + manager.getTasks());
        Task getId = manager.getTaskById(1);
        System.out.println("Определенная задача под первым айди: " + getId);
        Task update = new Task("Переезд", "Собирать коробки", task1.getTaskId(), Status.IN_PROGRESS);
        System.out.println("Новое обновление всех задач " + manager.updateTask(update));
        manager.removeTask(1);
        System.out.println("Получение всех задач после удаление по первому айди" + manager.getTasks());
        manager.removeAllTAsk();
        System.out.println("После удаления всех задач:" + manager.getTasks());
        System.out.println(manager.getEpics());
        manager.removeAllEpic();
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
    }
}