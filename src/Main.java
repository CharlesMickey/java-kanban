import java.io.IOException;
import java.time.LocalDateTime;
import manager.Managers;
import model.*;
import server.HttpTaskServer;
import server.KVServer;
import taskManager.TaskManager;

public class Main {

  public static void main(String[] args) throws IOException {
    KVServer kvs = new KVServer();
    kvs.start();
    //    kvs.stop();

    TaskManager taskManager = Managers.getDefault(kvs.getServer());

    new HttpTaskServer(taskManager).startServer();

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
      epic2.getId(),
      50,
      LocalDateTime.of(2023, 7, 25, 15, 30)
    );

    taskManager.setTask(task.getId(), task);
    taskManager.setTask(task2.getId(), task2);

    taskManager.setEpic(epic1.getId(), epic1);
    taskManager.setEpic(epic2.getId(), epic2);

    taskManager.setSubtask(subtask1.getId(), subtask1);
    taskManager.setSubtask(subtask2.getId(), subtask2);
    taskManager.setSubtask(subtask3.getId(), subtask3);

    taskManager.getTask(1);
    taskManager.getTask(2);
    taskManager.getEpic(3);
    taskManager.getEpic(4);
    taskManager.getSubtask(5);
    taskManager.getSubtask(6);
    taskManager.getSubtask(7);

    System.out.println("\nПо порядку становись");
    System.out.println(taskManager.getPrioritizedTasks());

    System.out.println("\nВесь список задач");
    System.out.println(taskManager.getAllEpics());
    System.out.println(taskManager.getAllTasks());
    System.out.println(taskManager.getAllSubtasks());

    System.out.println("\n История");
    System.out.println(taskManager.getHistory());
  }
}
