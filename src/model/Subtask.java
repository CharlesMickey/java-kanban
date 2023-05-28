package model;

public class Subtask extends Task {

  // Забыл добавить private

  private String epicId;

  public Subtask(
    String name,
    String description,
    Status status,
    String epicId,
    String id
  ) {
    super(name, description, status, id);
    this.epicId = epicId;
  }

  public Subtask(
    String name,
    String description,
    Status status,
    String epicId
  ) {
    super(name, description, status);
    this.epicId = epicId;
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
    return (
      "\n model.Subtask{" +
      "epicId=" +
      epicId +
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
      '}'
    );
  }
}
