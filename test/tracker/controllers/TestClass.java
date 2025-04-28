package tracker.controllers;

import main.java.tracker.controllers.impl.InMemoryTaskManager;
import main.java.tracker.model.Epic;
import main.java.tracker.model.Subtask;
import main.java.tracker.util.Status;
import main.java.tracker.util.Type;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TestClass {
    @Test
    public void testEpicStatus() {
        InMemoryTaskManager managers = new InMemoryTaskManager();
        Epic epic = new Epic(1, Type.EPIC, "Сходить на обед", Status.NEW, "Одеться",
                LocalDateTime.now());
        managers.createEpic(epic);
        Subtask subtask = new Subtask(2, Type.SUBTASK, "Купить сок", Status.NEW,
                "Сходить в магазин", epic.getTaskId(), LocalDateTime.now());
        managers.createSubtask(subtask);
        Subtask subtask1 = new Subtask(3, Type.SUBTASK, "Сменить прическу", Status.DONE,
                "Записаться в барбершоп", epic.getTaskId(), LocalDateTime.now());
        managers.createSubtask(subtask1);

        Epic getEpicId = managers.getEpicById(epic.getTaskId());
        assertEquals(Status.NEW, getEpicId.getStatus());
    }

    @Test
    public void testFileNotFound() {
        assertThrows(IOException.class, () -> {
            Files.readAllLines(Path.of("data.txt"));
        });
    }

    @Test
    public void testFileCreation() {
        assertDoesNotThrow(() -> {
            Path tempFile = Files.createTempFile("data", ".csv");
            Files.writeString(tempFile, "Цель, попасть в FAANG!");
        });
    }
}
