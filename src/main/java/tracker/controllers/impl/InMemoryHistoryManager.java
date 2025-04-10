package main.java.tracker.controllers.impl;

import main.java.tracker.controllers.HistoryManager;
import main.java.tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private final Map<Integer, Node> historyMap;

    public InMemoryHistoryManager() {
        historyMap = new HashMap<>();
    }

    @Override
    public boolean add(Task task) {
        if (task == null) {
            return false;
        }
        remove(task.getTaskId());
        linkLast(task);
        return true;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        final Node oldTail = new Node(task);
        if (tail == null) {
            head = oldTail;
        } else {
            tail.next = oldTail;
            oldTail.prev = tail;
        }
        tail = oldTail;
        historyMap.put(task.getTaskId(), tail);
    }

    private List<Task> getTasks() {
        List<Task> newTask = new ArrayList<>();
        Node curHead = head;
        while (curHead != null) {
            newTask.add(curHead.task);
            curHead = curHead.next;
        }
        return newTask;
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }

    private void removeNode(int id) {
        Node node = historyMap.get(id);
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        historyMap.remove(id);
    }
}

