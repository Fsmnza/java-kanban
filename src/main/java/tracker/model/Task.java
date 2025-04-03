package main.java.tracker.model;

import main.java.tracker.util.Status;
import main.java.tracker.util.Type;

import java.util.Objects;

public class Task {
    private int taskId;
    private String name;
    private String description;
    private Status status;
    private Type type;

    public Task(int taskId, Type type, String name, Status status, String description) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public Type getType() {
        return Type.TASK;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public int setTaskId(int taskId) {
        this.taskId = taskId;
        return taskId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return taskId == task.taskId && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, taskId, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskId=" + taskId +
                ", status=" + status +
                '}';
    }
}