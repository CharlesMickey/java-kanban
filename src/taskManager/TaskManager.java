package taskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import model.Epic;
import model.Subtask;
import model.Task;

public interface TaskManager {
  HashMap<Integer, Task> getTasks();

  Set<Task> getPrioritizedTasks();

  void setTask(Integer id, Task task);

  Boolean checkIntersection(Task task);

  HashMap<Integer, Epic> getEpics();

  void setEpic(Integer id, Epic epic);

  HashMap<Integer, Subtask> getSubtasks();

  void setSubtask(Integer id, Subtask subtask);

  ArrayList<Task> getAllEpics();

  ArrayList<Task> getAllTasks();

  ArrayList<Task> getAllSubtasks();

  void deleteTaskOfAnyTypeById(Integer id);

  void deleteAllSubtasks();

  void deleteAllEpics();

  void deleteAllTasks();

  Task getTaskOfAnyTypeById(Integer id);

  Task getTask(Integer id);

  Subtask getSubtask(Integer id);

  Epic getEpic(Integer id);

  void updateAnyTypeOfTask(Integer id, Task updatedTask);

  List<Task> getHistory();
}
