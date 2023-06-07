import manager.InMemoryTaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

public class Main {

  public static void main(String[] args) {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    Task task = new Task("В магаз", "Купить 5 шоколадок", Status.NEW);
    Task task2 = new Task("В Макдак", "Купить пару флурри и роял", Status.NEW);

    Epic epic1 = new Epic(
      "Подготовка к путешествию",
      "Подготовиться к поездке на озеро"
    );
    Epic epic2 = new Epic("Ремонт в квартире", "Сделать ремонт в падике");

    Subtask subtask1 = new Subtask(
      "Купить билеты",
      "Купить билеты на поезд в Анапу",
      Status.NEW,
      epic1.getId()
    );

    Subtask subtask2 = new Subtask(
      "Забронировать отель",
      "Забронировать номер в отеле в СПб",
      Status.NEW,
      epic1.getId()
    );

    Subtask subtask3 = new Subtask(
      "Купить краску",
      "Купить краску – фиолетовую",
      Status.NEW,
      epic2.getId()
    );

    inMemoryTaskManager.setTasks(task.getId(), task);
    inMemoryTaskManager.setTasks(task2.getId(), task2);

    inMemoryTaskManager.setEpics(epic1.getId(), epic1);
    inMemoryTaskManager.setEpics(epic2.getId(), epic2);

    inMemoryTaskManager.setSubtasks(subtask1.getId(), subtask1);
    inMemoryTaskManager.setSubtasks(subtask2.getId(), subtask2);
    inMemoryTaskManager.setSubtasks(subtask3.getId(), subtask3);

    inMemoryTaskManager.getTask(1);
    inMemoryTaskManager.getTask(2);
    inMemoryTaskManager.getEpic(3);
    inMemoryTaskManager.getEpic(4);
    inMemoryTaskManager.getSubtask(5);
    inMemoryTaskManager.getSubtask(6);
    inMemoryTaskManager.getSubtask(7);
    inMemoryTaskManager.getTask(1);
    inMemoryTaskManager.getTask(2);
    inMemoryTaskManager.getTask(1);
    inMemoryTaskManager.getTask(2);
    inMemoryTaskManager.getTask(1);
    inMemoryTaskManager.getTask(2);

    System.out.println("\nИстория");

    System.out.println(inMemoryTaskManager.getHistoryManager().getHistory());

    System.out.println("\nДобавили");
    System.out.println(inMemoryTaskManager.getEpics());
    System.out.println(inMemoryTaskManager.getTasks());
    System.out.println(inMemoryTaskManager.getSubtasks());

    System.out.println("\nОбновили");

    Subtask subtask3Upd = new Subtask(
      "Остаемся в Ленинграде",
      "Идем в Бикмагентс",
      Status.DONE,
      subtask3.getEpicId(),
      subtask3.getId()
    );

    Task taskUpd = new Task(
      "Лучше в погреб",
      "За свеклой",
      Status.IN_PROGRESS,
      task.getId()
    );

    inMemoryTaskManager.updateAnyTypeOfTask(subtask3.getId(), subtask3Upd);
    inMemoryTaskManager.updateAnyTypeOfTask(task.getId(), taskUpd);

    System.out.println("\nВесь список задач");

    System.out.println(inMemoryTaskManager.getAllEpics());
    System.out.println(inMemoryTaskManager.getAllTasks());
    System.out.println(inMemoryTaskManager.getAllSubtasks());

    inMemoryTaskManager.deleteTaskOfAnyTypeById(epic1.getId());
    inMemoryTaskManager.deleteTaskOfAnyTypeById(task2.getId());
    inMemoryTaskManager.deleteTaskOfAnyTypeById(subtask3.getId());

    System.out.println("\nУдалили");

    System.out.println(inMemoryTaskManager.getEpics());
    System.out.println(inMemoryTaskManager.getTasks());
    System.out.println(inMemoryTaskManager.getSubtasks());
  }
}
