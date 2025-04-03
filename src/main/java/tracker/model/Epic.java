package main.java.tracker.model;

import main.java.tracker.util.Status;
import main.java.tracker.util.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Epic extends Task {
    List<Integer> subtaskIds;

    public Epic(int id, Type type, String name,Status status, String description) {
        super(id, type, name,status, description);
        subtaskIds = new ArrayList<>();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Epic epic = (Epic) object;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toString() {
        return super.toString() +
                "Epic{" +
                "subtaskId=" + subtaskIds +
                '}';
    }
}