package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

  private List<Subtask> subtasks;
  private Status status;

  public Epic(Type type, String name, String description) {
    super(type, name, description);
    calculateStatus();
  }

  public Epic(Type type, String name, String description, Status status, int id) {
    super(type, name, description, status, id);
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
  }

  public void deleteSubtask(Subtask subtask) {
    if (subtask != null) {
      subtasks.remove(subtask);
      calculateStatus();
    }
  }

  public void deleteAllSubtask() {
    if (subtasks != null) {
      subtasks.clear();
    }
    calculateStatus();
  }

  public void updateSubtask(Subtask oldSubtask, Subtask newSubtask) {
    int index = subtasks != null ? subtasks.indexOf(oldSubtask) : -1;
    if (index != -1) {
      subtasks.set(index, newSubtask);
    }
  }

  private void setStatus(Status status) {
    this.status = status;
  }

  @Override
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
      ", subtasks=" +
      subtasks +
      '}'
    );
  }
}
