package tracker.util;

import tracker.controllers.*;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManger manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task("Переезд", "Собирать коробки", Status.NEW);
        Task task2 = new Task("Купить хлеб", "Сходить в магазин", Status.IN_PROGRESS);
        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic("Переезд", "Подготовка к переезду", Status.IN_PROGRESS);
        manager.createEpic(epic1);

        Subtask sub1 = new Subtask("Собрать вещи", "Собрать все в коробки", Status.IN_PROGRESS, epic1.getTaskId());
        Subtask sub2 = new Subtask("Упаковать посуду", "Упакаовать все отдельно", Status.DONE, epic1.getTaskId());
        Subtask sub3 = new Subtask("Вызвать грущика", "Сделать обьявление", Status.NEW, epic1.getTaskId());
        manager.createSubtask(sub3);
        manager.createSubtask(sub2);
        manager.createSubtask(sub1);

        Epic epic2 = new Epic("Позвать коллег на корпоратив", "Написать в общий чат про это", Status.NEW);
        manager.createEpic(epic2);
        manager.getEpicById(7);
        manager.getSubtaskById(6);
        manager.getSubtaskById(5);
        manager.getSubtaskById(4);
        manager.getEpicById(7);
        manager.getEpicById(3);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getTaskById(1);

        System.out.println("История: " + manager.getHistory());
        System.out.println(manager.getHistory().size());
        historyManager.remove(3);
        historyManager.remove(4);
        historyManager.remove(6);
        historyManager.remove(5);
        System.out.println("Проверка на сабтаск " + manager.getSubtasks());
        System.out.println("История: " + manager.getHistory());
        System.out.println(manager.getHistory().size());


    }


}