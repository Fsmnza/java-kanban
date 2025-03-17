package tracker.model;

import tracker.util.Status;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subtaskId = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public void addSubtaskId(int subtaskIds) {
        subtaskId.add(subtaskIds);
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskId=" + subtaskId +
                '}';
    }
}