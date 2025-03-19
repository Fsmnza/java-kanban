package tracker.util;

import tracker.controllers.*;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManger manager = Managers.getDefault();
        Task task1 = new Task("Переезд", "Собирать коробки", Status.NEW);
        Task task2 = new Task("Купить хлеб", "Сходить в магазин", Status.IN_PROGRESS);
        manager.createTask(task1);
        manager.createTask(task2);
        Epic epic1 = new Epic("Переезд", "Подготовка к переезду", Status.NEW);
        manager.createEpic(epic1);
        Subtask sub3 = new Subtask("Собрать вещи", "Собрать все в коробки", Status.IN_PROGRESS, epic1.getTaskId());
        manager.createSubtask(sub3);
        Task task3 = manager.getTaskById(1);
        Epic epic3 = manager.getEpicById(3);
        Subtask subtask1 = manager.getSubtaskById(4);
        Subtask subtask2 = manager.getSubtaskById(3);
        Task task5 = manager.getTaskById(1);
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println("История: " + manager.getHistory());
    }
}