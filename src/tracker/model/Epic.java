package tracker.model;

import tracker.util.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    List<Integer> subtaskIds;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subtaskIds = new ArrayList<>();
    }
    public List<Integer> getSubtaskIds() {
        return subtaskIds;
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
        return super.toString()+
                "Epic{"  +
                 "subtaskId=" + subtaskIds +
                '}';
    }
}