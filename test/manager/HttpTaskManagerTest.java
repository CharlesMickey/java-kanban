package manager;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import model.Status;
import model.Task;
import model.Type;
import org.junit.jupiter.api.*;
import server.HttpTaskServer;
import server.KVServer;

class HttpTaskManagerTest {

  private static final String URL = "http://localhost:8078";
  private Gson gson = Managers.getGson();
  private static KVServer server;
  private static HttpTaskServer httpTaskServer;
  private static HttpTaskManager taskManager;
  private static Task task;

  @BeforeEach
  void setUp() throws IOException {
    task = new Task(Type.TASK, "В Макдак", "Купить пару флурри и роял", Status.NEW);
    server = new KVServer();
    server.start();
    taskManager = (HttpTaskManager) Managers.getDefault(server.getServer());
    httpTaskServer = new HttpTaskServer(taskManager);

    httpTaskServer.startServer();
  }

  @AfterEach
  void tearDown() {
    server.stop();
    httpTaskServer.stopServer();
  }

  @Test
  void save() throws IOException, InterruptedException {
    taskManager.setTask(task.getId(), task);
    int taskId = task.getId();

    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create("http://localhost:8081/tasks/task?id=" + taskId);
    HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    java.lang.reflect.Type taskType = new TypeToken<Task>() {}.getType();
    assertEquals(200, response.statusCode(), "Статус save код не 200");
    assertNotNull(response.body(), "Save body == 0");

    Task loadingTask = gson.fromJson(response.body(), taskType);
    assertTrue(loadingTask.equals(task), "Отправленный объект не равен полученному");
  }

  @Test
  void loadFromServer() throws IOException, InterruptedException {
    URI url = URI.create("http://localhost:8081/tasks/task");
    Task newTask = new Task(Type.TASK, "123123123", "123123123", Status.NEW);
    String json = gson.toJson(newTask);

    assertTrue(taskManager.getTasks().isEmpty(), "Список task не пустой");

    final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(url)
      .header("Content-type", "application/json")
      .POST(body)
      .build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

    assertEquals(201, response.statusCode(), "Статус код не 201");
    assertFalse(taskManager.getTasks().isEmpty(), "Список task  пустой");
    assertEquals(taskManager.getTask(newTask.getId()).getName(), newTask.getName(), "Имена задач не сопадают");
  }
}
