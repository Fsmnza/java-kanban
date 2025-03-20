package tracker.controllers.impl;

import tracker.controllers.HistoryManager;
import tracker.model.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public boolean add(Task task) { // null
        if (history.size() > 10) {
            history.removeFirst();
        }
        if (task == null) {
            return false;
        }
        return history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}