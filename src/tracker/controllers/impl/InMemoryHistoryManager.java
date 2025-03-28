package tracker.controllers.impl;

import tracker.controllers.HistoryManager;
import tracker.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history;
    private Node<Task> head;
    private Node<Task> tail;
    private final Map<Integer, Node<Task>> historyMap = new LinkedHashMap<>();

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public boolean add(Task task) {
        if (task == null) {
            return false;
        }
        remove(task.getTaskId());
        linkLast(task);
        if (history.size() > 10) {
            history.remove(0);
        }
        history.add(task);
        historyMap.put(task.getTaskId(), tail);
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
        final Node<Task> oldTail = tail;
        final Node<Task> oldNode = new Node<>(task);
        tail = oldNode;
        historyMap.put(task.getTaskId(), oldNode);
        if (oldTail == null) {
            head = oldNode;
        } else {
            oldTail.next = oldNode;
            oldNode.prev = oldTail;
        }
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
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i).getTaskId() == id) {
                history.remove(i);
                break;
            }
        }
    }
}

class Node<T> {
    T task;
    public Node<T> prev;
    public Node<T> next;

    public Node(T task) {
        this.task = task;
        this.prev = null;
        this.next = null;
    }
}