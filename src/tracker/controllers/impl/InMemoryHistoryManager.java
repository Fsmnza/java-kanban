package tracker.controllers.impl;

import tracker.controllers.HistoryManager;
import tracker.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> tail;
    private final Map<Integer, Node<Task>> historyMap;

    public InMemoryHistoryManager() {
        historyMap = new LinkedHashMap<>();
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

    @Override
    public void remove(int id) {
        removeNode(id);

    }

    public void linkLast(Task task) {
        final Node<Task> oldTail = new Node<>(task);
        if (tail == null) {
            head = oldTail;
        } else {
            tail.next = oldTail;
            oldTail.prev = tail;
        }
        tail = oldTail;
        historyMap.put(task.getTaskId(), oldTail);
    }

    public List<Task> getTasks() {
        List<Task> newTask = new ArrayList<>();
        Node<Task> curHead = head;
        while (curHead != null) {
            newTask.add(curHead.task);
            curHead = curHead.next;
        }
        return newTask;
    }

    private void removeNode(int id) {
        Node<Task> node = historyMap.get(id);
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
