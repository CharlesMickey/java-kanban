package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task> {

  private static int idCount = 0;
  private Type type;
  private int id;
  private String name;
  private String description;
  private Status status;
  private int duration;
  private LocalDateTime startTime;

  public Task(
    Type type,
    String name,
    String description,
    Status status,
    int id,
    int duration,
    LocalDateTime startTime
  ) {
    this.type = type;
    this.id = id;
    this.name = name;
    this.description = description;
    this.status = status;
    this.duration = duration;
    this.startTime = startTime;
    idCount = idCount > id ? idCount : id;
  }

  public Task(Type type, String name, String description, Status status, int duration, LocalDateTime startTime) {
    this.type = type;
    this.id = idCount + 1;
    idCount++;
    this.name = name;
    this.description = description;
    this.status = status;
    this.duration = duration;
    this.startTime = startTime;
  }

  public Task(Type type, String name, String description, Status status, int id) {
    this.type = type;
    this.id = id;
    this.name = name;
    this.description = description;
    this.status = status;
    idCount = idCount > id ? idCount : id;
  }

  public Task(Type type, String name, String description, Status status) {
    this.type = type;
    this.id = idCount + 1;
    idCount++;
    this.name = name;
    this.description = description;
    this.status = status;
  }

  public Task(Type type, String name, String description) {
    this.type = type;
    this.id = idCount + 1;
    idCount++;
    this.name = name;
    this.description = description;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getEndTime() {
    if (startTime == null || duration < 0) {
      return null;
    }
    return startTime.plusMinutes(duration);
  }

  public Type getType() {
    return type;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Status getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return (
      "\nmodel.Task{" +
      "id=" +
      id +
      ", name='" +
      name +
      '\'' +
      ", description='" +
      description +
      '\'' +
      ", status=" +
      status +
      ", startTime=" +
      startTime +
      ", duration=" +
      duration +
      '}'
    );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Task task = (Task) o;
    return (
      id == task.id &&
      type == task.type &&
      Objects.equals(name, task.name) &&
      Objects.equals(description, task.description) &&
      status == task.status
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, id, name, description, status);
  }

  @Override
  public int compareTo(Task o) {
    if (o.startTime == null && this.startTime == null) {
      return Integer.compare(this.id, o.getId());
    } else if (this.startTime == null) {
      return 1;
    } else if (o.startTime == null) {
      return -1;
    } else {
      int startTimeComparison = this.startTime.compareTo(o.startTime);
      if (startTimeComparison != 0) {
        return startTimeComparison;
      } else {
        return Integer.compare(this.id, o.getId());
      }
    }
  }
}
