import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> epicId = new ArrayList<>();

    public Epic(String name, String description, int taskId, Status status) {
        super(name, description, taskId, status);
    }

    public ArrayList<Integer> getEpicId() {
        return epicId;
    }

    //    public void setEpicId(int epicId) {
//        this.epicId = epicId;
//    }
    public void addSubtaskId(int id) {
        epicId.add(id);
    }

    public void removeSubtaskId(int id) {
        epicId.remove(Integer.valueOf(id));
    }

    @Override
    public String toString() {
        return "Epic{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskId=" + taskId +
                ", status=" + status +
                '}';
    }
}