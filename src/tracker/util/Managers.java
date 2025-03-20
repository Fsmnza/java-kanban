package tracker.util;

import tracker.controllers.HistoryManager;
import tracker.controllers.impl.InMemoryHistoryManager;
import tracker.controllers.impl.InMemoryTaskManager;
import tracker.controllers.TaskManger;

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
