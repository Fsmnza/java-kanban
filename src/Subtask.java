public class Subtask extends Task {
    int subtaskId;
    public Subtask(String name, String description, int taskId, Status status, int subtaskId) {
        super(name, description,taskId, status);
        this.subtaskId = subtaskId;
    }

    public int getSubtaskId() {
        return subtaskId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "subtaskId=" + subtaskId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskId=" + taskId +
                ", status=" + status +
                '}';
    }
}