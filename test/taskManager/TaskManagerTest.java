package taskManager;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class TaskManagerTest<T extends TaskManager> {

  protected T taskManager;

  @BeforeEach
  protected void addInitialData() {
    taskManager = createTaskManager();
  }

  protected abstract T createTaskManager();

  @Test
  void getTasks() {
    final HashMap<Integer, Task> tasks = taskManager.getTasks();

    assertTrue(tasks.isEmpty(), "Список задач не пуст");

    Task task = new Task(Type.TASK, "В магаз", "Купить 5 шоколадок", Status.NEW);
    taskManager.setTask(task.getId(), task);

    assertFalse(tasks.isEmpty(), "Список задач пуст");
  }

  @Test
  void setTask() {
    Task task = new Task(Type.TASK, "В магаз", "Купить 5 шоколадок", Status.NEW);
    taskManager.setTask(task.getId(), task);

    final Task savedTask = taskManager.getTask(task.getId());

    assertNotNull(savedTask, "Задача не найдена.");
    assertEquals(task, savedTask, "Задачи не совпадают.");

    final HashMap<Integer, Task> tasks = taskManager.getTasks();

    assertNotNull(tasks, "Задачи на возвращаются.");
    assertEquals(1, tasks.size(), "Неверное количество задач.");
    assertNotEquals(task, tasks.get(0), "Задачи совпадают.");
  }

  @Test
  void getEpics() {
    final HashMap<Integer, Epic> epics = taskManager.getEpics();

    assertTrue(epics.isEmpty(), "Список эпиков не пуст");

    Epic epic = new Epic(Type.EPIC, "Подготовка к путешествию", "Подготовиться к поездке на озеро");
    taskManager.setEpic(epic.getId(), epic);

    assertFalse(epics.isEmpty(), "Список эпиков пуст");
  }

  @Test
  void setEpic() {
    Epic epic = new Epic(Type.EPIC, "Подготовка к путешествию", "Подготовиться к поездке на озеро");
    taskManager.setEpic(epic.getId(), epic);

    final Epic savedEpic = taskManager.getEpic(epic.getId());

    assertNotNull(savedEpic, "Эпик не найдена.");
    assertEquals(epic, savedEpic, "Эпики не совпадают.");
    assertEquals(epic.getStatus(), Status.NEW, "У эпика не верный статус.");

    final HashMap<Integer, Epic> epics = taskManager.getEpics();

    assertNotNull(epics, "Эпики на возвращаются.");
    assertEquals(1, epics.size(), "Неверное количество эпиков.");
    assertNotEquals(epic, epics.get(0), "Эпики совпадают.");
  }

  @Test
  void getSubtasks() {
    final HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();

    assertTrue(subtasks.isEmpty(), "Список подзадач не пуст");

    Epic epic = new Epic(Type.EPIC, "Ремонт в квартире", "Сделать ремонт в падике");
    taskManager.setEpic(epic.getId(), epic);

    Subtask subtask = new Subtask(
      Type.SUBTASK,
      "Купить билеты",
      "Купить билеты на поезд в Анапу",
      Status.NEW,
      epic.getId()
    );

    taskManager.setSubtask(subtask.getId(), subtask);

    assertFalse(subtasks.isEmpty(), "Список подзадач пуст");
  }

  @Test
  void setSubtask() {
    Epic epic = new Epic(Type.EPIC, "Подготовка к путешествию", "Подготовиться к поездке на озеро");
    taskManager.setEpic(epic.getId(), epic);
    Subtask subtask = new Subtask(
      Type.SUBTASK,
      "Купить билеты",
      "Купить билеты на поезд в Анапу",
      Status.NEW,
      epic.getId()
    );

    taskManager.setSubtask(subtask.getId(), subtask);

    final Subtask savedSubtask = taskManager.getSubtask(subtask.getId());

    assertNotNull(savedSubtask, "Подзадача не найдена.");
    assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");
    assertNotNull(taskManager.getEpics().get(subtask.getEpicId()), "Нет эпика стаким id.");

    final HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
    assertNotNull(subtasks, "Подзадачи на возвращаются.");
    assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
    assertNotEquals(subtask, subtasks.get(0), "Подзадачи совпадают.");
  }

  @Test
  void getAllEpics() {
    Epic epic = new Epic(Type.EPIC, "Подготовка к путешествию", "Подготовиться к поездке на озеро");

    assertTrue(taskManager.getAllEpics().isEmpty(), "Список эпиков не пустой");

    taskManager.setEpic(epic.getId(), epic);

    assertFalse(taskManager.getAllEpics().isEmpty(), "Список эпиков пустой");
    assertEquals(taskManager.getAllEpics().size(), 1, "В списке эпиков не 1 элемент");
  }

  @Test
  void getAllTasks() {
    Task task = new Task(Type.TASK, "В магаз", "Купить 5 шоколадок", Status.NEW);

    assertTrue(taskManager.getAllTasks().isEmpty(), "Список задач не пустой");

    taskManager.setTask(task.getId(), task);

    assertFalse(taskManager.getAllTasks().isEmpty(), "Список задач пустой");
    assertEquals(taskManager.getAllTasks().size(), 1, "В списке задач не 1 элемент");
  }

  @Test
  void getAllSubtasks() {
    Epic epic = new Epic(Type.EPIC, "Подготовка к путешествию", "Подготовиться к поездке на озеро");
    Subtask subtask = new Subtask(
      Type.SUBTASK,
      "Купить билеты",
      "Купить билеты на поезд в Анапу",
      Status.NEW,
      epic.getId()
    );

    assertTrue(taskManager.getAllSubtasks().isEmpty(), "Список подзадач не пустой");

    taskManager.setEpic(epic.getId(), epic);
    taskManager.setSubtask(subtask.getId(), subtask);

    assertFalse(taskManager.getAllSubtasks().isEmpty(), "Список подзадач пустой");
    assertEquals(taskManager.getAllSubtasks().size(), 1, "В списке подзадач не 1 элемент");
  }

  @Test
  void deleteTaskOfAnyTypeById() {
    Epic epic = new Epic(Type.EPIC, "Подготовка к путешествию", "Подготовиться к поездке на озеро");
    Subtask subtask = new Subtask(
      Type.SUBTASK,
      "Купить билеты",
      "Купить билеты на поезд в Анапу",
      Status.NEW,
      epic.getId()
    );
    Task task = new Task(Type.TASK, "В магаз", "Купить 5 шоколадок", Status.NEW);
    taskManager.setEpic(epic.getId(), epic);
    taskManager.setSubtask(subtask.getId(), subtask);
    taskManager.setTask(task.getId(), task);

    final HashMap<Integer, Task> tasks = taskManager.getTasks();
    final HashMap<Integer, Epic> epics = taskManager.getEpics();
    final HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();

    assertEquals(tasks.size(), 1, "В списке задач для удаления по id не 1 элемент");
    assertEquals(epics.size(), 1, "В списке эпиков для удаления по id не 1 элемент");
    assertEquals(subtasks.size(), 1, "В списке подзадач для удаления по id не 1 элемент");

    taskManager.deleteTaskOfAnyTypeById(task.getId());
    taskManager.deleteTaskOfAnyTypeById(epic.getId());
    taskManager.deleteTaskOfAnyTypeById(subtask.getId());

    assertEquals(tasks.size(), 0, "В списке задач для удаления по id не 0 элементов");
    assertEquals(epics.size(), 0, "В списке эпиков для удаления по id не 0 элементов");
    assertEquals(subtasks.size(), 0, "В списке подзадач для удаления по id не 0 элементов");
  }

  @Test
  void deleteAllSubtasks() {
    Epic epic = new Epic(Type.EPIC, "Подготовка к путешествию", "Подготовиться к поездке на озеро");
    Subtask subtask = new Subtask(
      Type.SUBTASK,
      "Купить билеты",
      "Купить билеты на поезд в Анапу",
      Status.NEW,
      epic.getId()
    );

    taskManager.setEpic(epic.getId(), epic);
    taskManager.setSubtask(subtask.getId(), subtask);

    final HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();

    assertEquals(epic.getSubtasks().size(), 1, "У эпика не 1 подзадача");
    assertEquals(subtasks.size(), 1, "В списке подзадач для удаления не 1 элемент");

    taskManager.deleteAllSubtasks();

    assertEquals(epic.getSubtasks().size(), 0, "У эпика не 0 подзадач");
    assertEquals(subtasks.size(), 0, "В списке подзадач для удаления не 0 элементов");
  }

  @Test
  void deleteAllEpics() {
    Epic epic = new Epic(Type.EPIC, "Подготовка к путешествию", "Подготовиться к поездке на озеро");
    Subtask subtask = new Subtask(
      Type.SUBTASK,
      "Купить билеты",
      "Купить билеты на поезд в Анапу",
      Status.NEW,
      epic.getId()
    );

    taskManager.setEpic(epic.getId(), epic);
    taskManager.setSubtask(subtask.getId(), subtask);

    final HashMap<Integer, Epic> epics = taskManager.getEpics();
    final HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();

    assertEquals(epics.size(), 1, "В списке эпиков для удаления не 1 элемент");
    assertEquals(subtasks.size(), 1, "В списке подзадач  (удаление эпиков)  не 1 элемент");

    taskManager.deleteAllEpics();

    assertEquals(epics.size(), 0, "В списке эпиков для удаления не 0 элемент");
    assertEquals(subtasks.size(), 0, "В списке подзадач после уделния эпиков не 0 элементов");
  }

  @Test
  void deleteAllTasks() {
    Task task = new Task(Type.TASK, "В магаз", "Купить 5 шоколадок", Status.NEW);

    taskManager.setTask(task.getId(), task);

    final HashMap<Integer, Task> tasks = taskManager.getTasks();

    assertEquals(tasks.size(), 1, "В списке задач не 1 элемент");

    taskManager.deleteTaskOfAnyTypeById(task.getId());

    assertTrue(tasks.isEmpty(), "После удаления все задач список не пустой");
  }

  @Test
  void getTaskOfAnyTypeById() {
    Epic epic = new Epic(Type.EPIC, "Подготовка к путешествию", "Подготовиться к поездке на озеро");
    Subtask subtask = new Subtask(
      Type.SUBTASK,
      "Купить билеты",
      "Купить билеты на поезд в Анапу",
      Status.NEW,
      epic.getId()
    );
    Task task = new Task(Type.TASK, "В магаз", "Купить 5 шоколадок", Status.NEW);
    taskManager.setEpic(epic.getId(), epic);
    taskManager.setSubtask(subtask.getId(), subtask);
    taskManager.setTask(task.getId(), task);

    Task savedEpic = taskManager.getTaskOfAnyTypeById(epic.getId());
    Task savedSubtask = taskManager.getTaskOfAnyTypeById(subtask.getId());
    Task savedTask = taskManager.getTaskOfAnyTypeById(task.getId());

    assertEquals(epic, savedEpic, "Полученные по id эпики не совпадают");
    assertEquals(subtask, savedSubtask, "Полученные по id подзадачи не совпадают");
    assertEquals(task, savedTask, "Полученные по id задачи не совпадают");
  }
}
