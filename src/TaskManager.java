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
    subtasks.put(id, subtask);
  }

  public ArrayList<Task> getAllEpics() {
    ArrayList<Task> allEpics = new ArrayList<>();
    for (Task epic : epics.values()) {
      allEpics.add(epic);
    }

    return allEpics;
  }

  public ArrayList<Task> getAllTasks() {
    ArrayList<Task> allTasks = new ArrayList<>();

    for (Task task : tasks.values()) {
      allTasks.add(task);
    }

    return allTasks;
  }

  public ArrayList<Task> getAllSubtasks() {
    ArrayList<Task> allSubtasks = new ArrayList<>();

    for (Task subtask : subtasks.values()) {
      allSubtasks.add(subtask);
    }
    return allSubtasks;
  }

  private void removeSubtasksByEpicId(String epicId) {
    subtasks.values().removeIf(subtask -> subtask.getEpicId().equals(epicId));
  }

  public void deleteTaskById(String id) {
    Task task = getTaskById(id);

    if (task instanceof Epic) {
      removeSubtasksByEpicId(id);
      epics.remove(id);
    } else if (task instanceof Subtask) {
      subtasks.remove(id);
      Epic epic = epics.get(((Subtask) task).getEpicId());
      if (epic != null) {
        epic.deleteSubtask((Subtask) task);
      }
    } else {
      tasks.remove(id);
    }
  }

  public void deleteAllSubtasks() {
    subtasks.clear();
  }

  public void deleteAllEpics() {
    epics.clear();
    deleteAllSubtasks();
  }

  public void deleteAllTasks() {
    tasks.clear();
  }

  public Task getTaskById(String id) {
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
