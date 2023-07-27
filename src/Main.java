import java.time.LocalDateTime;
import manager.Managers;
import model.*;
import taskFileManager.FileBackedTasksManager;
import taskManager.TaskManager;

public class Main {

  public static void main(String[] args) {
    TaskManager fileBackedTasksManager = Managers.getDefault();

    Task task = new Task(
      Type.TASK,
      "В магаз",
      "Купить 5 шоколадок",
      Status.NEW,
      30,
      LocalDateTime.of(2023, 7, 25, 15, 30)
    );
    Task task2 = new Task(Type.TASK, "В Макдак", "Купить пару флурри и роял", Status.NEW);

    Epic epic1 = new Epic(Type.EPIC, "Подготовка к путешествию", "Подготовиться к поездке на озеро");
    Epic epic2 = new Epic(Type.EPIC, "Ремонт в квартире", "Сделать ремонт в падике");

    Subtask subtask1 = new Subtask(
      Type.SUBTASK,
      "Купить билеты",
      "Купить билеты на поезд в Анапу",
      Status.NEW,
      epic1.getId(),
      32,
      LocalDateTime.of(1986, 8, 19, 18, 29)
    );

    Subtask subtask2 = new Subtask(
      Type.SUBTASK,
      "Забронировать отель",
      "Забронировать номер в отеле в СПб",
      Status.NEW,
      epic1.getId(),
      30,
      LocalDateTime.of(2025, 6, 25, 15, 31)
    );

    Subtask subtask3 = new Subtask(
      Type.SUBTASK,
      "Купить краску",
      "Купить краску – фиолетовую",
      Status.DONE,
      epic2.getId()
    );

    fileBackedTasksManager.setTask(task.getId(), task);
    fileBackedTasksManager.setTask(task2.getId(), task2);

    fileBackedTasksManager.setEpic(epic1.getId(), epic1);
    fileBackedTasksManager.setEpic(epic2.getId(), epic2);

    fileBackedTasksManager.setSubtask(subtask1.getId(), subtask1);
    fileBackedTasksManager.setSubtask(subtask2.getId(), subtask2);
    fileBackedTasksManager.setSubtask(subtask3.getId(), subtask3);

    fileBackedTasksManager.getTask(1);
    fileBackedTasksManager.getTask(2);
    fileBackedTasksManager.getEpic(3);
    fileBackedTasksManager.getEpic(4);
    fileBackedTasksManager.getSubtask(5);
    fileBackedTasksManager.getSubtask(6);
    fileBackedTasksManager.getSubtask(7);

    System.out.println("\nПо порядку становись");
    System.out.println(fileBackedTasksManager.getPrioritizedTasks());

    System.out.println("\nВесь список задач");
    System.out.println(fileBackedTasksManager.getAllEpics());
    System.out.println(fileBackedTasksManager.getAllTasks());
    System.out.println(fileBackedTasksManager.getAllSubtasks());

    System.out.println("\n История");
    System.out.println(fileBackedTasksManager.getHistory());

    FileBackedTasksManager fbtm = FileBackedTasksManager.loadFromFile("./resources/data.csv");

    System.out.println("\nВесь список восстановленных задач");
    System.out.println(fbtm.getAllEpics());
    System.out.println(fbtm.getAllTasks());
    System.out.println(fbtm.getAllSubtasks());

    System.out.println("\nВосстановленная история");
    System.out.println(fbtm.getHistory());

    System.out.println("\nПо порядку после восстановления становись");
    System.out.println(fbtm.getPrioritizedTasks());
  }
}
