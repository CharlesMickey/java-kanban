import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private ArrayList<Subtask> subtasks;

    public Epic(
            String name,
            String description,
            Status status,
            ArrayList<Subtask> subtasks
    ) {
        super(name, description, status);
        this.subtasks = subtasks;

        for (Subtask subtask : this.subtasks) {
            subtask.setEpicId(getId());
        }
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void deleteSubtask(Subtask subtask) {
        if (subtask != null) {
            this.subtasks.remove(subtask);
        }
    }

    @Override
    public String toString() {
        return "Epic{" + "subtasks=" + subtasks + '}';
    }
}
