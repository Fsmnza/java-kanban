package tracker.controllers.impl;

import tracker.model.Task;

public class Node {
    public Task task;
    public Node next;
    public Node prev;

    public Node(Task task) {
        this.task = task;
    }
}