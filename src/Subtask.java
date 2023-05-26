public class Subtask extends Task {

    String epicId;

    public Subtask(String name, String description, Status status, String id, String epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status) {
        super(name, description, status);
    }

    public String getEpicId() {
        if (epicId != null) {
            return epicId;
        }
        return "";
    }

    public void setEpicId(String epicId) {
        this.epicId = epicId;
    }


    @Override
    public String toString() {
        return "\n Subtask{"
                + "epicId=" + epicId +
                ", id=" +
                super.getId() +
                ", name='" +
                super.getName() +
                '\'' +
                ", description='" +
                super.getDescription() +
                '\'' +
                ", status=" +
                super.getStatus() +
                '}';
    }
}
