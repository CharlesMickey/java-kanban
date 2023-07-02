package taskManager;

import historyManager.HistoryManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import manager.Managers;
import model.Epic;
import model.Subtask;
import model.Task;

public class InMemoryTaskManager implements TaskManager {

  private final HistoryManager historyManager = Managers.getDefaultHistory();
  private final HashMap<Integer, Task> tasks;
  private final HashMap<Integer, Epic> epics;
  private final HashMap<Integer, Subtask> subtasks;

  public InMemoryTaskManager() {
    tasks = new HashMap<>();
    epics = new HashMap<>();
    subtasks = new HashMap<>();
  }

  @Override
  public HashMap<Integer, Task> getTasks() {
    return tasks;
  }

  @Override
  public void setTask(Integer id, Task task) {
    tasks.put(id, task);
  }

  @Override
  public HashMap<Integer, Epic> getEpics() {
    return epics;
  }

  @Override
  public void setEpic(Integer id, Epic epic) {
    epics.put(id, epic);
  }

  @Override
  public HashMap<Integer, Subtask> getSubtasks() {
    return subtasks;
  }

  @Override
  public void setSubtask(Integer id, Subtask subtask) {
    Task task = getTaskOfAnyTypeById(subtask.getEpicId());
    if (task != null && task instanceof Epic) {
      Epic epic = (Epic) task;
      epic.setSubtask(subtask);
      subtasks.put(id, subtask);
    } else {
      System.out.println("Нет эпика с таким id");
    }
  }

  @Override
  public ArrayList<Task> getAllEpics() {
    return new ArrayList<>(epics.values());
  }

  @Override
  public ArrayList<Task> getAllTasks() {
    return new ArrayList<>(tasks.values());
  }

  @Override
  public ArrayList<Task> getAllSubtasks() {
    return new ArrayList<>(subtasks.values());
  }

  private void removeSubtasksByEpicId(Integer epicId) {
    subtasks.values().removeIf(subtask -> {
      if(subtask.getEpicId().equals(epicId)) {
        historyManager.remove(subtask.getId());
        return true;
      } else {
        return false;
      }
    });
  }

  @Override
  public void deleteTaskOfAnyTypeById(Integer id) {
    historyManager.remove(id);
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

  @Override
  public void deleteAllSubtasks() {
    subtasks.clear();
    for (Epic epic : epics.values()) {
      epic.deleteAllSubtask();
      epic.calculateStatus();
    }
  }

  @Override
  public void deleteAllEpics() {
    epics.clear();
    subtasks.clear();
  }

  @Override
  public void deleteAllTasks() {
    tasks.clear();
  }

  @Override
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

  @Override
  public Task getTask(Integer id) {
    Task task = tasks.get(id);
    if (task != null) {
      historyManager.add(task);
    }
    return task;
  }

  @Override
  public Subtask getSubtask(Integer id) {
    Subtask subtask = subtasks.get(id);
    if (subtask != null) {
      historyManager.add(subtask);
    }
    return subtask;
  }

  @Override
  public Epic getEpic(Integer id) {
    Epic epic = epics.get(id);
    if (epic != null) {
      historyManager.add(epic);
    }
    return epic;
  }

  @Override
  public List<Subtask> getSubtasksByEpicId(String epicId) {
    return epics.get(epicId).getSubtasks();
  }

  @Override
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

  @Override
  public List<Task> getHistory() {
    return historyManager.getHistory();
  }
}
