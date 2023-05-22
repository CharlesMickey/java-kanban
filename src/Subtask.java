public class Subtask extends Task {

    String epicId;

    public Subtask(String name, String description, Status status) {
        super(name, description, status);
    }

    public String getEpicId() {
        return epicId;
    }

    public void setEpicId(String epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" + "epicId=" + epicId + '}';
    }
}
