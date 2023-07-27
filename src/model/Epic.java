package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

  private List<Subtask> subtasks;
  private Status status;
  private int duration;
  private LocalDateTime startTime;
  private LocalDateTime endTime;

  public Epic(Type type, String name, String description) {
    super(type, name, description);
    calculateStatus();
    calculateStartTime();
    calculateDuration();
    calculateEndTime();
  }

  public Epic(Type type, String name, String description, Status status, int id) {
    super(type, name, description, status, id);
    this.status = status;
  }

  public Epic(
    Type type,
    String name,
    String description,
    Status status,
    int id,
    int duration,
    LocalDateTime startTime
  ) {
    super(type, name, description, status, id);
    this.status = status;
    this.duration = duration;
    this.startTime = startTime;
  }

  public List<Subtask> getSubtasks() {
    return subtasks;
  }

  public void setSubtask(Subtask subtask) {
    if (subtasks == null) {
      subtasks = new ArrayList<>();
    }
    subtasks.add(subtask);
    calculateStatus();
    calculateStartTime();
    calculateDuration();
    calculateEndTime();
  }

  public void deleteSubtask(Subtask subtask) {
    if (subtask != null) {
      subtasks.remove(subtask);
      calculateStatus();
      calculateStartTime();
      calculateDuration();
      calculateEndTime();
    }
  }

  public void deleteAllSubtask() {
    if (subtasks != null) {
      subtasks.clear();
    }
    calculateStatus();
    calculateStartTime();
    calculateDuration();
    calculateEndTime();
  }

  public void updateSubtask(Subtask oldSubtask, Subtask newSubtask) {
    int index = subtasks != null ? subtasks.indexOf(oldSubtask) : -1;
    if (index != -1) {
      subtasks.set(index, newSubtask);
    }
  }

  @Override
  public int getDuration() {
    return duration;
  }

  @Override
  public void setDuration(int duration) {
    this.duration = duration;
  }

  @Override
  public LocalDateTime getStartTime() {
    return startTime;
  }

  @Override
  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  @Override
  public LocalDateTime getEndTime() {
    return endTime;
  }

  public void calculateDuration() {
    if (subtasks == null || subtasks.isEmpty() || allSubtaskWithoutDuration()) {
      setDuration(0);
    } else {
      setDuration(getAllSubtaskDuration());
    }
  }

  public void calculateStartTime() {
    if (subtasks == null || subtasks.isEmpty() || allSubtaskWithoutStartTime()) {
      setStartTime(null);
    } else {
      setStartTime(getEarliestSubtaskStartTime());
    }
  }

  public void calculateEndTime() {
    if (subtasks == null || subtasks.isEmpty() || allSubtaskWithoutStartTime()) {
      setEndTime(null);
    } else {
      setEndTime(getSubtaskLatestEndTime());
    }
  }

  private void setStatus(Status status) {
    this.status = status;
  }

  public Status getStatus() {
    return status;
  }

  public void calculateStatus() {
    if (subtasks == null || subtasks.isEmpty() || allSubtasksNew()) {
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

  private boolean allSubtaskWithoutDuration() {
    for (Subtask subtask : subtasks) {
      if (subtask.getDuration() > 0) {
        return false;
      }
    }
    return true;
  }

  private boolean allSubtaskWithoutStartTime() {
    for (Subtask subtask : subtasks) {
      if (subtask.getStartTime() != null) {
        return false;
      }
    }
    return true;
  }

  private int getAllSubtaskDuration() {
    int allDuration = 0;
    for (Subtask subtask : subtasks) {
      if (subtask.getDuration() > 0) {
        allDuration += subtask.getDuration();
      }
    }
    return allDuration;
  }

  private LocalDateTime getEarliestSubtaskStartTime() {
    LocalDateTime earliestStartTime = null;
    for (Subtask subtask : subtasks) {
      LocalDateTime subtaskStartTime = subtask.getStartTime();
      if (subtaskStartTime != null && (earliestStartTime == null || subtaskStartTime.isBefore(earliestStartTime))) {
        earliestStartTime = subtaskStartTime;
      }
    }
    return earliestStartTime;
  }

  private LocalDateTime getSubtaskLatestEndTime() {
    LocalDateTime latestEndTime = null;
    for (Subtask subtask : subtasks) {
      LocalDateTime subtaskEndTime = subtask.getEndTime();
      if (subtaskEndTime != null && (latestEndTime == null || subtaskEndTime.isAfter(latestEndTime))) {
        latestEndTime = subtaskEndTime;
      }
    }
    return latestEndTime;
  }

  @Override
  public String toString() {
    return (
      "\n model.Epic{" +
      "id=" +
      getId() +
      ", name='" +
      getName() +
      '\'' +
      ", description='" +
      getDescription() +
      '\'' +
      ", status=" +
      status +
      ", startTime=" +
      getStartTime() +
      ", endTime=" +
      getEndTime() +
      ", duration=" +
      getDuration() +
      ", subtasks=" +
      subtasks +
      '}'
    );
  }
}
