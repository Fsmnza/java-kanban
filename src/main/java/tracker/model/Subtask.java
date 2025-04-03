package main.java.tracker.model;

import main.java.tracker.util.Status;
import main.java.tracker.util.Type;

import java.util.Objects;

public class Subtask extends Task {
    int epicId;

    public Subtask(int id, Type type, String name, Status status, String description, int epicId) {
        super(id, type, name, status, description);
        this.epicId = epicId;
    }

    public int getEpicID() {
        return epicId;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Subtask subtask = (Subtask) object;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }


    @Override
    public String toString() {
        return super.toString() +
                "Subtask{" +
                "epicId=" + epicId +
                '}';
    }
}