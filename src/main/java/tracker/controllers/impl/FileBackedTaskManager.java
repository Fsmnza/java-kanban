package main.java.tracker.controllers.impl;

import main.java.tracker.exceptions.ManagerSaveException;
import main.java.tracker.model.Epic;
import main.java.tracker.model.Subtask;
import main.java.tracker.model.Task;
import main.java.tracker.util.Status;
import main.java.tracker.util.Type;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(String newFile) {
        this.file = new File(newFile);
    }

    public void save() {
        Path temporaryFile = null;
        try {
            temporaryFile = Files.createTempFile("data", ".csv");
            try (BufferedWriter br = Files.newBufferedWriter(temporaryFile, StandardCharsets.UTF_8)) {
                br.write("id,type,name,status,description,epic");
                br.newLine();
                for (Task tasks : task.values()) {
                    br.write(toString(tasks));
                    br.newLine();
                }
                for (Epic epics : epic.values()) {
                    br.write(toString(epics));
                    br.newLine();
                }
                for (Subtask subtasks : subtask.values()) {
                    br.write(toString(subtasks));
                    br.newLine();
                }
            }
            Files.move(temporaryFile, file.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            throw new ManagerSaveException("Ошибка при сохранении задач: " + exception.getMessage());
        }
    }

    @Override
    public int createTask(Task tasks) {
        int id = super.createTask(tasks);
        save();
        return id;
    }

    @Override
    public int createSubtask(Subtask subtasks) {
        int id = super.createSubtask(subtasks);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epics) {
        int id = super.createEpic(epics);
        save();
        return id;
    }

    public String toString(Task tasks) {
//        String line;
        if (tasks.getType() == Type.SUBTASK) {
            Subtask subtasks = (Subtask) tasks;
            return String.format("%d,%s,%s,%s,%s,%d",
                    subtasks.getTaskId(),
                    subtasks.getType(),
                    subtasks.getName(),
                    subtasks.getStatus(),
                    subtasks.getDescription(),
                    subtasks.getEpicID()
            );
        } else {
            return String.format("%d,%s,%s,%s,%s",
                    tasks.getTaskId(),
                    tasks.getType(),
                    tasks.getName(),
                    tasks.getStatus(),
                    tasks.getDescription()
            );
        }
    }

    public static Task fromString(String value) {
        String[] newFromString = value.split(",");
        int id = Integer.parseInt(newFromString[0]);
        Type type = Type.valueOf(newFromString[1]);
        String name = newFromString[2];
        Status status = Status.valueOf(newFromString[3]);
        String description = newFromString[4];

        switch (type) {
            case TASK:
                return new Task(id, type, name, status, description);
            case EPIC:
                return new Epic(id, type, name, status, description);
            case SUBTASK:
                int epicId = Integer.parseInt(newFromString[5]);
                return new Subtask(id, type, name, status, description, epicId);
            default:
                throw new RuntimeException("Неизвестный тип задачи: " + type);
        }
    }


    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.getPath());
        Path files = file.toPath();
        int maxId = 0;
        List<String> lines = Files.readAllLines(files, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            return fileBackedTaskManager;
        } else {
            for (int i = 1; i < lines.size(); i++) {
                String taskLine = lines.get(i);
                Task task = fromString(taskLine);
                int taskId = task.getTaskId();
                maxId = Math.max(maxId, taskId);
                if (task.getType() == Type.EPIC) {
                    fileBackedTaskManager.epic.put(taskId, (Epic) task);
                } else if (task instanceof Subtask) {
                    fileBackedTaskManager.subtask.put(taskId, (Subtask) task);
                } else {
                    fileBackedTaskManager.task.put(taskId, task);
                }
            }
        }
        fileBackedTaskManager.setNextTaskId(maxId + 1);
        return fileBackedTaskManager;
    }
//
//    public static void clearFile(String filePath) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
//            writer.write("");
//        } catch (IOException exception) {
//            throw new ManagerSaveException("Ошибка при очистке файла: " + exception.getMessage());
//        }
//    }

    public static void main(String[] args) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager("data.csv");
//        String newFilePath = "data.csv";
//        clearFile(newFilePath);
//        System.out.println("Файл очищен.");
        try (BufferedWriter writer = new BufferedWriter(Files.newBufferedWriter(Path.of("data.csv"), StandardCharsets.UTF_8))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Csv файл до введение информации:");
        try {
            System.out.println(Files.readString(new File("data.csv").toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Task task1 = new Task(1, Type.TASK, "Приготовить обед", Status.NEW, "Почистить картошку");
        manager.createTask(task1);

        Epic epic1 = new Epic(2, Type.EPIC, "Переезд", Status.NEW, "Собрать вещи");
        manager.createEpic(epic1);

        Subtask subtask1 = new Subtask(3, Type.SUBTASK, "Собрать коробки", Status.NEW, "Найти коробки", epic1.getTaskId());
        manager.createSubtask(subtask1);

        System.out.println("Csv файл после введение информации:");
        try {
            System.out.println(Files.readString(new File("data.csv").toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileBackedTaskManager loadedManager = loadFromFile(new File("data.csv"));
        System.out.println("Csv файл после чтение информации:");
        for (Task task : loadedManager.task.values()) {
            System.out.println(task);
        }
        for (Subtask subtask2 : loadedManager.subtask.values()) {
            System.out.println(subtask2);
        }
        for (Epic epic2 : loadedManager.epic.values()) {
            System.out.println(epic2);
        }

    }
}