package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.Epic;
import model.Subtask;
import model.Task;

public interface TaskManager {
  public HashMap<Integer, Task> getTasks();

  public void setTasks(Integer id, Task task);

  public HashMap<Integer, Epic> getEpics();

  public void setEpics(Integer id, Epic epic);

  public HashMap<Integer, Subtask> getSubtasks();

  public void setSubtasks(int id, Subtask subtask);

  public ArrayList<Task> getAllEpics();

  public ArrayList<Task> getAllTasks();

  public ArrayList<Task> getAllSubtasks();

  public void deleteTaskOfAnyTypeById(Integer id);

  public void deleteAllSubtasks();

  public void deleteAllEpics();

  public void deleteAllTasks();

  public Task getTaskOfAnyTypeById(Integer id);

  public Task getTask(Integer id);

  public Subtask getSubtask(Integer id);

  public Epic getEpic(Integer id);

  public List<Subtask> getSubtasksByEpicId(String epicId);

  public void updateAnyTypeOfTask(Integer id, Task updatedTask);
}
