package model;

import java.time.LocalDateTime;

public class Subtask extends Task {

  private Integer epicId;

  public Subtask(
    Type type,
    String name,
    String description,
    Status status,
    Integer epicId,
    Integer id,
    int duration,
    LocalDateTime startTime
  ) {
    super(type, name, description, status, id, duration, startTime);
    this.epicId = epicId;
  }

  public Subtask(
    Type type,
    String name,
    String description,
    Status status,
    Integer epicId,
    int duration,
    LocalDateTime startTime
  ) {
    super(type, name, description, status, duration, startTime);
    this.epicId = epicId;
  }

  public Subtask(Type type, String name, String description, Status status, Integer epicId, Integer id) {
    super(type, name, description, status, id);
    this.epicId = epicId;
  }

  public Subtask(Type type, String name, String description, Status status, Integer epicId) {
    super(type, name, description, status);
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
      ", startTime=" +
      super.getStartTime() +
      ", duration=" +
      super.getDuration() +
      '}'
    );
  }
}
