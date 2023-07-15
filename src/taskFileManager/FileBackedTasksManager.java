package taskFileManager;

import currentThrows.ManagerSaveException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import model.*;
import taskManager.InMemoryTaskManager;

public class FileBackedTasksManager extends InMemoryTaskManager {

  private final Path filePath;
  private final String CSV_HEAD = "id,type,name,status,description,epic";

  public FileBackedTasksManager(String defaultFilePath) {
    this.filePath = Paths.get(defaultFilePath);
  }

  public static FileBackedTasksManager loadFromFile(String path) {
    FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(path);
    try (BufferedReader fileData = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)) {
      fileData.readLine();
      String line = fileData.readLine();
      while (fileData.ready()) {
        if (line.isEmpty()) {
          break;
        }

        Task task = fromStringTask(line);
        if (task instanceof Epic) {
          Epic epic = (Epic) task;
          fileBackedTasksManager.setEpic(epic.getId(), epic);
        } else if (task instanceof Subtask) {
          Subtask subtask = (Subtask) task;
          fileBackedTasksManager.setSubtask(subtask.getId(), subtask);
        } else {
          fileBackedTasksManager.setTask(task.getId(), task);
        }
        line = fileData.readLine();
      }

      line = fileData.readLine();
      if (line == null) {
        return fileBackedTasksManager;
      }

      for (int taskId : historyFromString(line)) {
        Task task = fileBackedTasksManager.getTaskOfAnyTypeById(taskId);
        if (task instanceof Epic) {
          fileBackedTasksManager.getEpic(taskId);
        } else if (task instanceof Subtask) {
          fileBackedTasksManager.getSubtask(taskId);
        } else {
          fileBackedTasksManager.getTask(taskId);
        }
      }
    } catch (IOException exception) {
      throw new ManagerSaveException("Произошла ошибка при получении данных в файл.", exception);
    }

    return fileBackedTasksManager;
  }

  private void save() {
    try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
      writer.write(CSV_HEAD);
      writer.newLine();

      for (Task task : getAllTasks()) {
        writer.write(toStringTask(task));
        writer.newLine();
      }
      for (Task epic : getAllEpics()) {
        writer.write(toStringTask(epic));
        writer.newLine();
      }
      for (Task subtask : getAllSubtasks()) {
        writer.write(toStringTask(subtask));
        writer.newLine();
      }

      writer.newLine();

      writer.write(historyToString(getHistory()));
    } catch (IOException exception) {
      throw new ManagerSaveException("Произошла ошибка при сохранении данных в файл.", exception);
    }
  }

  static String historyToString(List<Task> tasks) {
    StringBuilder history = new StringBuilder("");

    for (Task historyTask : tasks) {
      history.append(historyTask.getId()).append(",");
    }
    if (history.length() > 0) {
      history.setLength(history.length() - 1);
    }

    return history.toString();
  }

  static List<Integer> historyFromString(String value) {
    ArrayList<Integer> historyIds = new ArrayList<>();

    for (String strId : value.split(",")) {
      historyIds.add(Integer.parseInt(strId));
    }

    return historyIds;
  }

  private String toStringTask(Task task) {
    if (task instanceof Subtask) {
      Subtask subtask = (Subtask) task;
      return String.format(
        "%d,%s,%s,%s,%s,%d",
        subtask.getId(),
        subtask.getType(),
        subtask.getName(),
        subtask.getStatus(),
        subtask.getDescription(),
        subtask.getEpicId()
      );
    } else if (task instanceof Epic) {
      Epic epic = (Epic) task;
      return String.format(
        "%d,%s,%s,%s,%s",
        epic.getId(),
        epic.getType(),
        epic.getName(),
        epic.getStatus(),
        epic.getDescription()
      );
    } else {
      return String.format(
        "%d,%s,%s,%s,%s",
        task.getId(),
        task.getType(),
        task.getName(),
        task.getStatus(),
        task.getDescription()
      );
    }
  }

  private static Task fromStringTask(String value) {
    String[] taskStr = value.split(",");

    int id = Integer.parseInt(taskStr[0]);
    String name = taskStr[2];
    Status status = Status.valueOf(taskStr[3]);
    String description = taskStr[4];

    if (taskStr[1].equals(Type.TASK.name())) {
      return new Task(Type.TASK, name, description, status, id);
    } else if (taskStr[1].equals(Type.EPIC.name())) {
      return new Epic(Type.EPIC, name, description, status, id);
    } else {
      int epicId = Integer.parseInt(taskStr[5]);
      return new Subtask(Type.SUBTASK, name, description, status, epicId, id);
    }
  }

  @Override
  public void setTask(Integer id, Task task) {
    super.setTask(id, task);
    save();
  }

  @Override
  public void setEpic(Integer id, Epic epic) {
    super.setEpic(id, epic);
    save();
  }

  @Override
  public void setSubtask(Integer id, Subtask subtask) {
    super.setSubtask(id, subtask);
    save();
  }

  @Override
  public void deleteTaskOfAnyTypeById(Integer id) {
    super.deleteTaskOfAnyTypeById(id);
    save();
  }

  @Override
  public void deleteAllSubtasks() {
    super.deleteAllSubtasks();
    save();
  }

  @Override
  public void deleteAllEpics() {
    super.deleteAllEpics();
    save();
  }

  @Override
  public void deleteAllTasks() {
    super.deleteAllTasks();
    save();
  }

  @Override
  public Task getTask(Integer id) {
    super.getTask(id);
    save();
    return null;
  }

  @Override
  public Subtask getSubtask(Integer id) {
    super.getSubtask(id);
    save();
    return null;
  }

  @Override
  public Epic getEpic(Integer id) {
    super.getEpic(id);
    save();
    return null;
  }

  @Override
  public void updateAnyTypeOfTask(Integer id, Task updatedTask) {
    super.updateAnyTypeOfTask(id, updatedTask);
    save();
  }
}
