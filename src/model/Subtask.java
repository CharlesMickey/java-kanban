package model;

public class Subtask extends Task {

  private Integer epicId;

  public Subtask(String name, String description, Status status, Integer epicId, Integer id
  ) {
    super(name, description, status, id);
    this.epicId = epicId;
  }

  public Subtask(String name, String description, Status status, Integer epicId
  ) {
    super(name, description, status);
    this.epicId = epicId;
  }

  public Integer getEpicId() {
    if (epicId != null) {
      return epicId;
    }
    return null;
  }

  public void setEpicId(Integer epicId) {
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
