package taskFileManager;

import currentThrows.ManagerSaveException;
import historyManager.HistoryManager;
import manager.Managers;
import model.*;
import taskManager.InMemoryTaskManager;
import taskManager.TaskManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

  private  final Path filePath;
  private final HistoryManager historyManager = Managers.getDefaultHistory();
  private final String CSV_HEAD = "id,type,name,status,description,epic";

  public FileBackedTasksManager(String defaultFilePath) {
    this.filePath = Paths.get(defaultFilePath);
  }

  private void save()  {
    try(BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
      writer.write(CSV_HEAD);
      writer.newLine();

      for (Task task: getAllTasks()) {
        writer.write(toString(task));
        writer.newLine();
      }
      for (Task epic: getAllEpics()) {
        writer.write(toString(epic));
        writer.newLine();
      }
      for (Task subtask: getAllSubtasks()) {
        writer.write(toString(subtask));
        writer.newLine();
      }

      writer.newLine();

      writer.write(historyToString(historyManager));

    } catch (IOException exception) {
       throw new ManagerSaveException("Произошла ошибка при сохранении данных в файл.");
    }
  }

  static String historyToString(HistoryManager manager) {
    StringBuilder history = new StringBuilder("");

    for(Task historyTask: manager.getHistory()) {
      history.append(historyTask.getId() + ",");
    }
    if(history.length() > 0) {
      history.setLength(history.length() - 1);
    }

    return history.toString();
  }

  private String toString(Task task) {
    if( task instanceof Subtask) {
      Subtask subtask = (Subtask) task;
      return String.format("%d, %s, %s, %s, %s, %d", subtask.getId(), subtask.getType(), subtask.getName(),
      subtask.getStatus(), subtask.getDescription(), subtask.getEpicId());
    } else if (task instanceof Epic) {
    Epic epic = (Epic) task;
    return String.format("%d,%s,%s,%s,%s,", epic.getId(), epic.getType(), epic.getName(), epic.getStatus(),
            epic.getDescription());
    } else {
    return String.format("%d,%s,%s,%s,%s,", task.getId(), task.getType(), task.getName(), task.getStatus(),
            task.getDescription());
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
  public void updateAnyTypeOfTask(Integer id, Task updatedTask) {
    super.updateAnyTypeOfTask(id, updatedTask);
    save();
  }

  public static  void main(String[] args) {

    FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("./resources/data.csv");
    Task task = new Task(
            Type.TASK,
            "В магаз",
            "Купить 5 шоколадок",
            Status.NEW
    );
    Task task2 = new Task(
            Type.TASK,
            "В Макдак",
            "Купить пару флурри и роял",
            Status.NEW
    );

    Epic epic1 = new Epic(
            Type.EPIC,
            "Подготовка к путешествию",
            "Подготовиться к поездке на озеро"
    );
    Epic epic2 = new Epic(
            Type.EPIC,
            "Ремонт в квартире",
            "Сделать ремонт в падике"
    );

    Subtask subtask1 = new Subtask(
            Type.SUBTASK,
            "Купить билеты",
            "Купить билеты на поезд в Анапу",
            Status.NEW,
            epic1.getId()
    );

    Subtask subtask2 = new Subtask(
            Type.SUBTASK,
            "Забронировать отель",
            "Забронировать номер в отеле в СПб",
            Status.NEW,
            epic1.getId()
    );

    Subtask subtask3 = new Subtask(
            Type.SUBTASK,
            "Купить краску",
            "Купить краску – фиолетовую",
            Status.NEW,
            epic2.getId()
    );

    fileBackedTasksManager.setTask(task.getId(), task);
    fileBackedTasksManager.setTask(task2.getId(), task2);

    fileBackedTasksManager.setEpic(epic1.getId(), epic1);
    fileBackedTasksManager.setEpic(epic2.getId(), epic2);

    fileBackedTasksManager.setSubtask(subtask1.getId(), subtask1);
    fileBackedTasksManager.setSubtask(subtask2.getId(), subtask2);
    fileBackedTasksManager.setSubtask(subtask3.getId(), subtask3);

    Subtask subtask3Upd = new Subtask(
            Type.SUBTASK,
            "Остаемся в Ленинграде",
            "Идем в Бикмагентс",
            Status.DONE,
            subtask3.getEpicId(),
            subtask3.getId()
    );

    Task taskUpd = new Task(
            Type.TASK,
            "Лучше в погреб",
            "За свеклой",
            Status.IN_PROGRESS,
            task.getId()
    );

    fileBackedTasksManager.updateAnyTypeOfTask(subtask3.getId(), subtask3Upd);
    fileBackedTasksManager.updateAnyTypeOfTask(task.getId(), taskUpd);

  }
}
