import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {

  private HashMap<String, Task> tasks;
  private HashMap<String, Epic> epics;
  private HashMap<String, Subtask> subtasks;

  public TaskManager() {
    tasks = new HashMap<>();
    epics = new HashMap<>();
    subtasks = new HashMap<>();
  }

  public HashMap<String, Task> getTasks() {
    return tasks;
  }

  public void setTasks(String id, Task task) {
    tasks.put(id, task);
  }

  public HashMap<String, Epic> getEpics() {
    return epics;
  }

  public void setEpics(String id, Epic epic) {
    epics.put(id, epic);
  }

  public HashMap<String, Subtask> getSubtasks() {
    return subtasks;
  }

  public void setSubtasks(String id, Subtask subtask) {
    Epic epic = (Epic) getTaskOfAnyTypeById(subtask.getEpicId());
    if (epic != null) {
      epic.setSubtask(subtask);
      subtasks.put(id, subtask);
    }
    System.out.println("Нет эпика с таким id");
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

  private void removeSubtasksByEpicId(String epicId) {
    subtasks.values().removeIf(subtask -> subtask.getEpicId().equals(epicId));
  }

  public void deleteTaskOfAnyTypeById(String id) {
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

  public Task getTaskOfAnyTypeById(String id) {
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

  public void updateAnyTypeOfTask(String id, Task updatedTask) {
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
