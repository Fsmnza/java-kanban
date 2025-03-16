import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        HashMap<Integer, Task> task = new HashMap<>();
        HashMap<Integer, Subtask> subtask = new HashMap<>();
        HashMap<Integer, Epic> epic = new HashMap<>();
        Manager manager = new Manager(task, subtask,epic);

        Task task1 = new Task("Переезд", "Собирать коробки", Manager.idCounter(), Status.NEW);
        Task task2 = new Task("Купить хлеб", "Сходить в магазин", Manager.idCounter(), Status.IN_PROGRESS);
        Epic epic1 = new Epic("Переезд", "Подготовка к переезду", Manager.idCounter(), Status.NEW);
        manager.createEpic(epic1);
        Subtask sub3 = new Subtask("Собрать вещи", "Собрать все в коробки", Manager.idCounter(), Status.IN_PROGRESS, epic1.getTaskId());
        manager.createSubtask(sub3);
        System.out.println("Проверка "+manager.getAllTask());
        Subtask updateSub = new Subtask("Забрать документы", "ьездить в университет", Manager.idCounter(), Status.DONE,epic1.getTaskId());
        manager.removeSubtask(3);
        System.out.println(manager.updateSubtask(updateSub));
        System.out.println("Проверка2 "+manager.getAllTask());
        manager.createTask(task1);
        manager.createTask(task2);
        Subtask sub1 = new Subtask("Собрать вещи", "Собрать все в коробки", Manager.idCounter(), Status.NEW, epic1.getTaskId());
        Subtask sub2 = new Subtask("Упаковать технику", "Осторожно сложить технику", Manager.idCounter(), Status.NEW, epic1.getTaskId());
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);
        System.out.println("Эпик без подзадач: " + manager.getEpicById(epic1.getTaskId()));
        System.out.println("Все задачи: " + manager.getAllTask());

        Task getId = manager.getTaskById(1);
        System.out.println("Определенная задача под первым айди: " + getId);
        Task update = new Task("Переезд", "Собирать коробки", task1.taskId, Status.IN_PROGRESS);
        System.out.println("Новое обновление всех задач "+manager.updateTask(update));
        manager.removeTask(1);
        System.out.println("Получение всех задач после удаление по первому айди" + manager.getAllTask());
        manager.removeAll();
        System.out.println("После удаления всех задач:" +manager.getAllTask() );
    }
}