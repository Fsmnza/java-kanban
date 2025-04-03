package main.java.tracker.controllers.impl;

import main.java.tracker.model.Task;

public class Node {
    public Task task;
    public Node next;
    public Node prev;

    public Node(Task task) {
        this.task = task;
    }
}