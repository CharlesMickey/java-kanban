package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.Epic;
import model.Subtask;
import model.Task;

public class TaskManager {

  private HashMap<Integer, Task> tasks;
  private HashMap<Integer, Epic> epics;
  private HashMap<Integer, Subtask> subtasks;

  public TaskManager() {
    tasks = new HashMap<>();
    epics = new HashMap<>();
    subtasks = new HashMap<>();
  }

  public HashMap<Integer, Task> getTasks() {
    return tasks;
  }

  public void setTasks(Integer id, Task task) {
    tasks.put(id, task);
  }

  public HashMap<Integer, Epic> getEpics() {
    return epics;
  }

  public void setEpics(Integer id, Epic epic) {
    epics.put(id, epic);
  }

  public HashMap<Integer, Subtask> getSubtasks() {
    return subtasks;
  }

  public void setSubtasks(int id, Subtask subtask) {
    Task task =  getTaskOfAnyTypeById(subtask.getEpicId());
    if (task != null && task instanceof Epic) {
      Epic epic = (Epic) task;
      epic.setSubtask(subtask);
      subtasks.put(id, subtask);
    } else {
    System.out.println("Нет эпика с таким id");
    }
  }

  public ArrayList<Task> getAllEpics() {
    return new ArrayList<>(epics.values());
  }

  public ArrayList<Task> getAllTasks() {
    return new ArrayList<>(tasks.values());
  }

  public ArrayList<Task> getAllSubtasks() {
    return new ArrayList<>(subtasks.values());
  }

  private void removeSubtasksByEpicId(Integer epicId) {
    subtasks.values().removeIf(subtask -> subtask.getEpicId().equals(epicId));
  }

  public void deleteTaskOfAnyTypeById(Integer id) {
    Task task = getTaskOfAnyTypeById(id);

    if (task instanceof Epic) {
      removeSubtasksByEpicId(id);
      epics.remove(id);
    } else if (task instanceof Subtask) {
      Epic epic = epics.get(((Subtask) task).getEpicId());
      if (epic != null) {
        epic.deleteSubtask((Subtask) task);
      }
      subtasks.remove(id);
    } else {
      tasks.remove(id);
    }
  }

  public void deleteAllSubtasks() {
    subtasks.clear();
    for (Epic epic : epics.values()) {
      epic.deleteAllSubtask();
      epic.calculateStatus();
    }
  }

  /* Использовал deleteAllSubtasks чтобы не дублировать код.
  Но после внесенных изменений лучше .cleare() (при условии что subtask не существует без epic)  */

  public void deleteAllEpics() {
    epics.clear();
    subtasks.clear();
  }

  public void deleteAllTasks() {
    tasks.clear();
  }

  public Task getTaskOfAnyTypeById(Integer id) {
    Task task = tasks.get(id);
    if (task == null) {
      task = epics.get(id);
    }
    if (task == null) {
      task = subtasks.get(id);
    }
    return task;
  }

  public List<Subtask> getSubtasksById(String epicId) {
    return epics.get(epicId).getSubtasks();
  }

  public void updateAnyTypeOfTask(Integer id, Task updatedTask) {
    if (tasks.containsKey(id)) {
      tasks.put(id, updatedTask);
    } else if (epics.containsKey(id)) {
      epics.put(id, (Epic) updatedTask);
    } else if (subtasks.containsKey(id)) {
      Subtask oldSubtask = subtasks.get(id);
      subtasks.put(id, (Subtask) updatedTask);
      if (oldSubtask.getEpicId() != null) {
        Epic epic = epics.get(oldSubtask.getEpicId());
        if (epic != null) {
          epic.updateSubtask(oldSubtask, (Subtask) updatedTask);
          epic.calculateStatus();
        }
      }
    }
  }
}
