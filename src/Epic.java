import java.util.List;

public class Epic extends Task {

    private List<Subtask> subtasks;
    private Status status;

    public Epic(String name, String description, List<Subtask> subtasks) {
        super(name, description);
        this.subtasks = subtasks;
        calculateStatus();
        setEpicIdForSubtasks();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
        calculateStatus();
        setEpicIdForSubtasks();
    }

    public void deleteSubtask(Subtask subtask) {
        if (subtask != null) {
            subtasks.remove(subtask);
            calculateStatus();
        }
    }

    public void updateSubtask(Subtask oldSubtask, Subtask newSubtask) {
        int index = subtasks.indexOf(oldSubtask);
        if (index != -1) {
            subtasks.set(index, newSubtask);
        }
    }

    private void setStatus (Status status) {
        this.status = status;
    }

    public void calculateStatus() {
        if (subtasks.isEmpty() || allSubtasksNew()) {
            setStatus(Status.NEW);
        } else if (allSubtasksDone()) {
            setStatus(Status.DONE);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    private boolean allSubtasksNew() {
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() != Status.NEW) {
                return false;
            }
        }
        return true;
    }

    private boolean allSubtasksDone() {
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() != Status.DONE) {
                return false;
            }
        }
        return true;
    }

    private void setEpicIdForSubtasks() {
        for (Subtask subtask : subtasks) {
            subtask.setEpicId(getId());
        }
    }


    @Override
    public String toString() {
        return "\n Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + status +
                ", subtasks=" + subtasks +
                '}';
    }
}
