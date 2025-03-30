package tracker.controllers.impl;

import tracker.model.Task;

class Node {
    Task task;
    Node prev;
    Node next;

    Node(Task task) {
        this.task = task;
        this.prev = null;
        this.next = null;
    }
}