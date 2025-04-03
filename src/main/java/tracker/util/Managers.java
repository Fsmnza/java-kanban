package main.java.tracker.util;

import main.java.tracker.controllers.HistoryManager;
import main.java.tracker.controllers.impl.InMemoryHistoryManager;
import main.java.tracker.controllers.impl.InMemoryTaskManager;
import main.java.tracker.controllers.TaskManger;

public class Managers {
    private Managers() {
    }

    public static TaskManger getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
