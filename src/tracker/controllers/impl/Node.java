package tracker.controllers.impl;

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