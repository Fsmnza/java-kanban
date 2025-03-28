package tracker.controllers.impl;

import tracker.controllers.HistoryManager;
import tracker.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> tail;

    public InMemoryHistoryManager() {
    }

    @Override
    public boolean add(Task task) {
        if (task == null) {
            return false;
        }
        remove(task.getTaskId());
        linkLast(task);
        if (historyMap.size() > 10) {
            remove(head.task.getTaskId());
        }
        return true;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}

