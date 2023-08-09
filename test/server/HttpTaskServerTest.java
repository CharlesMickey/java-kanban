package server;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import manager.HttpTaskManager;
import manager.Managers;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HttpTaskServerTest {

  private static final String URL = "http://localhost:8081";
  private Gson gson = Managers.getGson();
  private static KVServer server;
  private static HttpTaskServer httpTaskServer;
  private static HttpTaskManager taskManager;
  private static Task task1;
  private static Task task2;
  private static Epic epic;
  private static Subtask subtask;

  @BeforeEach
  void setUp() throws IOException {
    task1 = new Task(Type.TASK, "В магаз", "Купить 5 шоколадок", Status.NEW, 30, LocalDateTime.of(1023, 7, 25, 15, 30));
    task2 = new Task(Type.TASK, "Вторая задача", "Описание 2", Status.NEW);
    epic = new Epic(Type.EPIC, "Ремонт в квартире", "Сделать ремонт в падике");
    subtask =
      new Subtask(
        Type.SUBTASK,
        "Купить билеты",
        "Купить билеты на поезд в Анапу",
        Status.NEW,
        epic.getId(),
        32,
        LocalDateTime.of(1986, 8, 19, 18, 29)
      );

    server = new KVServer();
    server.start();
    taskManager = (HttpTaskManager) Managers.getDefault(server.getServer());
    httpTaskServer = new HttpTaskServer(taskManager);

    taskManager.setTask(task1.getId(), task1);
    taskManager.setEpic(epic.getId(), epic);
    taskManager.setSubtask(subtask.getId(), subtask);

    httpTaskServer.startServer();
  }

  @AfterEach
  void tearDown() {
    server.stop();
    httpTaskServer.stopServer();
  }

  @Test
  void getAllTasks() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/task");
    HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, response.statusCode(), "Вернулся не 200 код");
    java.lang.reflect.Type taskType = new TypeToken<List<Task>>() {}.getType();
    List<Task> tasks = gson.fromJson(response.body(), taskType);

    assertNotNull(tasks, "Пустое поле при получении всех задач");
    assertEquals(1, tasks.size(), "Количество задач отличается от ожидаемого");
    assertEquals(task1, tasks.get(0), "Полученная и отправленная задачи не совпадают");
  }

  @Test
  void getTaskById() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/task?id=" + task1.getId());
    HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, response.statusCode(), "Вернулся не 200 код");
    java.lang.reflect.Type taskType = new TypeToken<Task>() {}.getType();
    Task task = gson.fromJson(response.body(), taskType);

    assertNotNull(task, "Пустое поле при получении задачи по id");
    assertEquals(task1, task, "Полученная и отправленная задачи не совпадают");
  }

  @Test
  void getAllEpics() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/epic");
    HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, response.statusCode(), "Вернулся не 200 код");
    java.lang.reflect.Type taskType = new TypeToken<List<Epic>>() {}.getType();
    List<Epic> tasks = gson.fromJson(response.body(), taskType);

    assertNotNull(tasks, "Пустое поле при получении всех epics");
    assertEquals(1, tasks.size(), "Количество epic отличается от ожидаемого");
    assertEquals(epic, tasks.get(0), "Полученная и отправленная epics не совпадают");
  }

  @Test
  void getEpicById() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/epic?id=" + epic.getId());
    HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, response.statusCode(), "Вернулся не 200 код");
    java.lang.reflect.Type taskType = new TypeToken<Epic>() {}.getType();
    Epic task = gson.fromJson(response.body(), taskType);

    assertNotNull(task, "Пустое поле при получении epics по id");
    assertEquals(epic, task, "Полученная и отправленная epics не совпадают");
  }

  @Test
  void getAllSubtasks() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/subtask");
    HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, response.statusCode(), "Вернулся не 200 код");
    java.lang.reflect.Type taskType = new TypeToken<List<Subtask>>() {}.getType();
    List<Subtask> tasks = gson.fromJson(response.body(), taskType);

    assertNotNull(tasks, "Пустое поле при получении всех задач");
    assertEquals(1, tasks.size(), "Количество задач отличается от ожидаемого");
    assertEquals(subtask, tasks.get(0), "Полученная и отправленная задачи не совпадают");
  }

  @Test
  void getSubtaskById() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/subtask?id=" + subtask.getId());
    HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, response.statusCode(), "Вернулся не 200 код");
    java.lang.reflect.Type taskType = new TypeToken<Subtask>() {}.getType();
    Subtask task = gson.fromJson(response.body(), taskType);

    assertNotNull(task, "Пустое поле при получении subtasks по id");
    assertEquals(subtask, task, "Полученная и отправленная subtasks не совпадают");
  }

  @Test
  void getPrioritizedTasks() throws IOException, InterruptedException {
    taskManager.setTask(task2.getId(), task2);

    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/prioritized");
    HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, response.statusCode(), "Вернулся не 200 код");
    java.lang.reflect.Type taskType = new TypeToken<List<Task>>() {}.getType();
    List<Task> tasks = gson.fromJson(response.body(), taskType);
    System.out.println(tasks);
    assertNotNull(tasks, "Пустое поле при получении истории");
    assertEquals(3, tasks.size(), "Количество задач в истории отличается от ожидаемого");
    assertEquals(tasks.get(0).getId(), task1.getId(), "Id tasks не совпадают");
    assertEquals(tasks.get(2).getId(), task2.getId(), "Id tasks не совпадают");
  }

  @Test
  void getHistory() throws IOException, InterruptedException {
    taskManager.setTask(task2.getId(), task2);
    taskManager.getTask(task2.getId());
    taskManager.getTask(task1.getId());
    taskManager.getEpic(epic.getId());
    taskManager.getSubtask(subtask.getId());

    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/history");
    HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, response.statusCode(), "Вернулся не 200 код");
    java.lang.reflect.Type taskType = new TypeToken<List<Task>>() {}.getType();
    List<Task> tasks = gson.fromJson(response.body(), taskType);

    assertNotNull(tasks, "Пустое поле при получении истории");
    assertEquals(4, tasks.size(), "Количество задач в истории отличается от ожидаемого");
    assertTrue(tasks.contains(task1), "Полученная и отправленная epics в истоии не совпадают");
  }

  @Test
  void deleteAllTasks() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/task");
    HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, response.statusCode(), "Вернулся не 200 код");
    java.lang.reflect.Type taskType = new TypeToken<List<Task>>() {}.getType();
    List<Task> tasks = gson.fromJson(response.body(), taskType);

    assertNotNull(tasks, "Пустое поле при получении всех задач");
    assertEquals(1, tasks.size(), "Количество задач отличается от ожидаемого");

    HttpRequest requestDelete = HttpRequest.newBuilder().uri(url).DELETE().build();
    HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, responseDelete.statusCode(), "Вернулся не 200 код");

    HttpRequest requestAfterDelete = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> responseAfterDelete = client.send(requestAfterDelete, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, responseAfterDelete.statusCode(), "Вернулся не 200 код");

    List<Task> tasksAfterDelete = gson.fromJson(responseAfterDelete.body(), taskType);

    assertNotNull(tasksAfterDelete, "Пустое поле при получении всех tasksAfterDelete");
    assertEquals(0, tasksAfterDelete.size(), "Количество задач после удаления не 0");
  }

  @Test
  void deleteAllEpics() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/epic");
    HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, response.statusCode(), "Вернулся не 200 код");
    java.lang.reflect.Type epicsType = new TypeToken<List<Task>>() {}.getType();
    List<Task> epics = gson.fromJson(response.body(), epicsType);

    assertNotNull(epics, "Пустое поле при получении всех epics");
    assertEquals(1, epics.size(), "Количество epics отличается от ожидаемого");

    HttpRequest requestDelete = HttpRequest.newBuilder().uri(url).DELETE().build();
    HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, responseDelete.statusCode(), "Вернулся не 200 код");

    HttpRequest requestAfterDelete = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> responseAfterDelete = client.send(requestAfterDelete, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, responseAfterDelete.statusCode(), "Вернулся не 200 код");

    List<Task> epicsAfterDelete = gson.fromJson(responseAfterDelete.body(), epicsType);

    assertNotNull(epicsAfterDelete, "Пустое поле при получении всех epicsAfterDelete");
    assertEquals(0, epicsAfterDelete.size(), "Количество epics после удаления не 0");
  }

  @Test
  void deleteAllSubtasks() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/subtask");
    HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, response.statusCode(), "Вернулся не 200 код");
    java.lang.reflect.Type subtaskType = new TypeToken<List<Task>>() {}.getType();
    List<Task> subtasks = gson.fromJson(response.body(), subtaskType);

    assertNotNull(subtasks, "Пустое поле при получении всех subtasks");
    assertEquals(1, subtasks.size(), "Количество subtasks отличается от ожидаемого");

    HttpRequest requestDelete = HttpRequest.newBuilder().uri(url).DELETE().build();
    HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, responseDelete.statusCode(), "Вернулся не 200 код");

    HttpRequest requestAfterDelete = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> responseAfterDelete = client.send(requestAfterDelete, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, responseAfterDelete.statusCode(), "Вернулся не 200 код");

    List<Task> subtasksAfterDelete = gson.fromJson(responseAfterDelete.body(), subtaskType);

    assertNotNull(subtasksAfterDelete, "Пустое поле при получении всех subtasksAfterDelete");
    assertEquals(0, subtasksAfterDelete.size(), "Количество subtasks после удаления не 0");
  }

  @Test
  void deleteTaskById() throws IOException, InterruptedException {
    taskManager.setTask(task2.getId(), task2);
    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/task");
    HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, response.statusCode(), "Вернулся не 200 код");
    java.lang.reflect.Type taskType = new TypeToken<List<Task>>() {}.getType();
    List<Task> tasks = gson.fromJson(response.body(), taskType);

    assertNotNull(tasks, "Пустое поле при получении всех задач");
    assertTrue(tasks.contains(task2), "Отсутствует task2");

    URI urlDeleteById = URI.create(URL + "/tasks/task?id=" + task2.getId());
    HttpRequest requestDelete = HttpRequest.newBuilder().uri(urlDeleteById).DELETE().build();
    HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, responseDelete.statusCode(), "Вернулся не 200 код");

    HttpRequest requestAfterDelete = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> responseAfterDelete = client.send(requestAfterDelete, HttpResponse.BodyHandlers.ofString());

    assertEquals(200, responseAfterDelete.statusCode(), "Вернулся не 200 код");

    List<Task> tasksAfterDelete = gson.fromJson(responseAfterDelete.body(), taskType);
    assertFalse(tasksAfterDelete.contains(task2), "Не удалилась task2");
  }

  @Test
  void addTask() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/task");
    String taskJson = gson.toJson(task2);
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(url)
      .header("Content-type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(taskJson))
      .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(201, response.statusCode(), "Вернулся не 201 код");

    HttpRequest requestAfterAdd = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> responseAfterAdd = client.send(requestAfterAdd, HttpResponse.BodyHandlers.ofString());
    java.lang.reflect.Type taskType = new TypeToken<List<Task>>() {}.getType();
    List<Task> tasks = gson.fromJson(responseAfterAdd.body(), taskType);

    assertNotNull(tasks, "Пустое поле при получении всех задач");
    assertEquals(2, tasks.size(), "Количество задач отличается от ожидаемого");
    assertTrue(tasks.contains(task2), "Нет созданой задачи");
  }

  @Test
  void addEpic() throws IOException, InterruptedException {
    Epic epic2 = new Epic(Type.EPIC, "Ремонт в квартире", "Сделать ремонт в падике");

    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/epic");
    String epicJson = gson.toJson(epic2);
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(url)
      .header("Content-type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(epicJson))
      .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(201, response.statusCode(), "Вернулся не 201 код");

    HttpRequest requestAfterAdd = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> responseAfterAdd = client.send(requestAfterAdd, HttpResponse.BodyHandlers.ofString());
    java.lang.reflect.Type taskType = new TypeToken<List<Epic>>() {}.getType();
    List<Epic> epics = gson.fromJson(responseAfterAdd.body(), taskType);

    assertNotNull(epics, "Пустое поле при получении всех epics");
    assertEquals(2, epics.size(), "Количество epics отличается от ожидаемого");
    assertTrue(epics.contains(epic2), "Нет созданой epic2");
  }

  @Test
  void addSubtask() throws IOException, InterruptedException {
    Subtask subtask2 = new Subtask(
      Type.SUBTASK,
      "Купить билеты",
      "Купить билеты на поезд в Анапу",
      Status.NEW,
      epic.getId()
    );

    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/subtask");
    String subtasksJson = gson.toJson(subtask2);
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(url)
      .header("Content-type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(subtasksJson))
      .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(201, response.statusCode(), "Вернулся не 201 код");

    HttpRequest requestAfterAdd = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> responseAfterAdd = client.send(requestAfterAdd, HttpResponse.BodyHandlers.ofString());
    java.lang.reflect.Type subtaskType = new TypeToken<List<Subtask>>() {}.getType();
    List<Subtask> subtasks = gson.fromJson(responseAfterAdd.body(), subtaskType);

    assertNotNull(subtasks, "Пустое поле при получении всех subtasks");
    assertEquals(2, subtasks.size(), "Количество subtasks отличается от ожидаемого");
    assertTrue(subtasks.contains(subtask2), "Нет созданой subtasks");
  }

  @Test
  void updateTask() throws IOException, InterruptedException {
    String UPDATE = "Update";
    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(URL + "/tasks/update");
    Task updTask = new Task(Type.TASK, task1.getName(), UPDATE, task1.getStatus(), task1.getId());
    String taskJson = gson.toJson(updTask);
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(url)
      .header("Content-type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(taskJson))
      .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(201, response.statusCode(), "Вернулся не 201 код");

    URI urlTaskById = URI.create(URL + "/tasks/task?id=" + updTask.getId());
    HttpRequest requestAfterUpdate = HttpRequest.newBuilder().uri(urlTaskById).GET().build();
    HttpResponse<String> responseAfterUpdate = client.send(requestAfterUpdate, HttpResponse.BodyHandlers.ofString());
    java.lang.reflect.Type taskType = new TypeToken<Task>() {}.getType();
    Task taskAfterUpd = gson.fromJson(responseAfterUpdate.body(), taskType);

    assertNotNull(taskAfterUpd, "Пустое поле при получении всех задач");
    assertEquals(taskAfterUpd.getDescription(), UPDATE, "Описание после обновления не совпадает");
  }

  @Test
  void stopServer() {}
}
