package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import manager.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import taskManager.TaskManager;

public class HttpTaskServer {

  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  private static final int PORT = 8081;
  private HttpServer server;
  private Gson gson;
  private TaskManager taskManager;

  public HttpTaskServer(TaskManager taskManager) throws IOException {
    this.gson = Managers.getGson();
    this.taskManager = taskManager;
    this.server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
    server.createContext("/tasks", this::handleTasks);
  }

  private void handleTasks(HttpExchange exchange) {
    try {
      String requestMethod = exchange.getRequestMethod();
      String path = exchange.getRequestURI().getPath();
      String query = exchange.getRequestURI().getQuery();

      switch (requestMethod) {
        case "GET":
          {
            if (Pattern.matches("^/tasks/task$", path) && query == null) {
              String allTasks = gson.toJson(taskManager.getAllTasks());
              writeText(exchange, allTasks, 200);
              return;
            }

            if (Pattern.matches("^/tasks/task", path) && Pattern.matches("^?id=\\d+$", query)) {
              String queryId = query.replaceFirst("^?id=", "");
              int id = parseIntPath(queryId);
              if (id != -1) {
                writeText(exchange, gson.toJson(taskManager.getTask(id)), 200);
                break;
              } else {
                System.out.println("Bad id: " + id);
                exchange.sendResponseHeaders(405, 0);
              }
            }

            if (Pattern.matches("^/tasks/subtask$", path) && query == null) {
              String allSubtasks = gson.toJson(taskManager.getAllSubtasks());
              writeText(exchange, allSubtasks, 200);
              return;
            }
            if (Pattern.matches("^/tasks/subtask", path) && Pattern.matches("^?id=\\d+$", query)) {
              String queryId = query.replaceFirst("^?id=", "");
              int id = parseIntPath(queryId);
              if (id != -1) {
                writeText(exchange, gson.toJson(taskManager.getSubtask(id)), 200);
                break;
              } else {
                System.out.println("Bad id: " + id);
                exchange.sendResponseHeaders(405, 0);
              }
            }

            if (Pattern.matches("^/tasks/epic", path) && query == null) {
              String allEpics = gson.toJson(taskManager.getAllEpics());
              writeText(exchange, allEpics, 200);
              return;
            }

            if (Pattern.matches("^/tasks/epic", path) && Pattern.matches("^?id=\\d+$", query)) {
              String queryId = query.replaceFirst("^?id=", "");
              int id = parseIntPath(queryId);
              if (id != -1) {
                writeText(exchange, gson.toJson(taskManager.getEpic(id)), 200);
                break;
              } else {
                System.out.println("Bad id: " + id);
                exchange.sendResponseHeaders(405, 0);
              }
            }
            if (Pattern.matches("^/tasks/prioritized", path) && query == null) {
              String allPrioritizedTasks = gson.toJson(taskManager.getPrioritizedTasks());
              writeText(exchange, allPrioritizedTasks, 200);
              return;
            }
            if (Pattern.matches("^/tasks/history", path) && query == null) {
              String historyTasks = gson.toJson(taskManager.getHistory());
              writeText(exchange, historyTasks, 200);
              return;
            }

            writeText(exchange, "Неверный эндпоинт", 405);
            break;
          }
        case "POST":
          {
            String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
            String requestBody = readText(exchange);

            if (contentType != null && contentType.equalsIgnoreCase("application/json") && requestBody != null) {
              try {
                gson.fromJson(requestBody, Object.class);
                switch (path) {
                  case "/tasks/task":
                    Task newTask = gson.fromJson(requestBody, Task.class);
                    if (
                      newTask.getName() == null ||
                      newTask.getType() == null ||
                      newTask.getDescription() == null ||
                      newTask.getStatus() == null
                    ) {
                      exchange.sendResponseHeaders(400, 0);
                      writeText(
                        exchange,
                        "Поля обязательные к заполнению у Задачи: Type, name, description, status. Задача не создана",
                        400
                      );

                      return;
                    }
                    if (taskManager.checkIntersection(newTask)) {
                      writeText(
                        exchange,
                        "При создании задачи обнаружено пересечение по времени. Задача не создана.",
                        400
                      );

                      return;
                    }
                    taskManager.setTask(newTask.getId(), newTask);
                    writeText(exchange, "Новая задача успешно добавлена.", 201);
                    return;
                  case "/tasks/epic":
                    Epic newEpic = gson.fromJson(requestBody, Epic.class);
                    if (newEpic.getName() == null || newEpic.getType() == null || newEpic.getDescription() == null) {
                      exchange.sendResponseHeaders(400, 0);
                      writeText(
                        exchange,
                        "Поля обязательные к заполению у Эпика: Type, name, description. Задача не создана",
                        400
                      );

                      return;
                    }
                    if (taskManager.checkIntersection(newEpic)) {
                      writeText(exchange, "При создании эпика обнаружено пересечение по времени. Эпик не создан.", 400);

                      return;
                    }
                    taskManager.setEpic(newEpic.getId(), newEpic);
                    writeText(exchange, "Новый эпик успешно добавлен.", 201);
                    return;
                  case "/tasks/subtask":
                    Subtask newSubtask = gson.fromJson(requestBody, Subtask.class);
                    if (
                      newSubtask.getName() == null ||
                      newSubtask.getType() == null ||
                      newSubtask.getDescription() == null ||
                      newSubtask.getStatus() == null ||
                      newSubtask.getEpicId() == null
                    ) {
                      exchange.sendResponseHeaders(400, 0);
                      writeText(
                        exchange,
                        "Поля обязательные к заполению у Задачи: Type, name, description, status, epicId. Задача не создана",
                        400
                      );

                      return;
                    }
                    if (taskManager.checkIntersection(newSubtask)) {
                      writeText(
                        exchange,
                        "При создании задачи обнаружено пересечение по времени. Задача не создана.",
                        400
                      );

                      return;
                    }
                    taskManager.setSubtask(newSubtask.getId(), newSubtask);
                    writeText(exchange, "Новая подзадача успешно добавлена.", 201);
                    return;
                  case "/tasks/update":
                    Task updateTask = gson.fromJson(requestBody, Task.class);
                    if (taskManager.checkIntersection(updateTask)) {
                      writeText(
                        exchange,
                        "При обновлении обнаружено пересечение по времени. Измените время и попробуйте еще раз.",
                        400
                      );

                      return;
                    }

                    if (updateTask instanceof Epic) {
                      if (
                        updateTask.getName() == null ||
                        updateTask.getType() == null ||
                        updateTask.getDescription() == null
                      ) {
                        exchange.sendResponseHeaders(400, 0);
                        writeText(
                          exchange,
                          "Поля обязательные к заполению у Эпика: Type, name, description. Задача не создана",
                          400
                        );
                      } else {
                        taskManager.setEpic(updateTask.getId(), (Epic) updateTask);
                        writeText(exchange, "Новый эпик успешно обновлен.", 201);
                      }
                      return;
                    }
                    if (updateTask instanceof Subtask) {
                      Subtask subtask = (Subtask) updateTask;
                      if (
                        subtask.getName() == null ||
                        subtask.getType() == null ||
                        subtask.getDescription() == null ||
                        subtask.getStatus() == null ||
                        subtask.getEpicId() == null
                      ) {
                        exchange.sendResponseHeaders(400, 0);
                        writeText(
                          exchange,
                          "Поля обязательные к заполению у Подзадачи: Type, name, description, status, epicId. Задача не создана",
                          400
                        );
                      } else {
                        taskManager.setTask(subtask.getId(), subtask);
                        writeText(exchange, "Новая подзадача успешно обновлена.", 201);
                      }
                      return;
                    }
                    if (updateTask instanceof Task) {
                      if (
                        updateTask.getName() == null ||
                        updateTask.getType() == null ||
                        updateTask.getDescription() == null ||
                        updateTask.getStatus() == null
                      ) {
                        exchange.sendResponseHeaders(400, 0);
                        writeText(
                          exchange,
                          "Поля обязательные к заполению у Задачи: Type, name, description, status. Задача не создана",
                          400
                        );
                      } else {
                        taskManager.setTask(updateTask.getId(), updateTask);
                        writeText(exchange, "Новая задача успешно обновлена.", 201);
                      }
                      return;
                    }
                  default:
                    writeText(exchange, "Неверный эндпоинт для POST запроса.", 201);
                    return;
                }
              } catch (JsonSyntaxException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(400, 0); // Bad Request - некорректный JSON
                return;
              }
            } else {
              System.out.println(45454545);
              exchange.sendResponseHeaders(400, 0); // Bad Request
              return;
            }
          }
        case "DELETE":
          {
            if (Pattern.matches("^/tasks/epic", path) && query == null) {
              taskManager.deleteAllEpics();
              String allEpics = gson.toJson(taskManager.getAllEpics());
              writeText(exchange, allEpics, 200);
              return;
            }
            if (Pattern.matches("^/tasks/task", path) && query == null) {
              taskManager.deleteAllTasks();
              String allEpics = gson.toJson(taskManager.getAllTasks());
              writeText(exchange, allEpics, 200);
              return;
            }
            if (Pattern.matches("^/tasks/subtask", path) && query == null) {
              taskManager.deleteAllSubtasks();
              String allEpics = gson.toJson(taskManager.getAllSubtasks());
              writeText(exchange, allEpics, 200);
              return;
            }
            if (Pattern.matches("^/tasks/task", path) && Pattern.matches("^?id=\\d+$", query)) {
              String queryId = query.replaceFirst("^?id=", "");
              int id = parseIntPath(queryId);
              if (id != -1) {
                System.out.println("Удалил task: " + id);
                taskManager.deleteTaskOfAnyTypeById(id);
                break;
              } else {
                System.out.println("Bad id: " + id);
                exchange.sendResponseHeaders(405, 0);
              }
            }

            break;
          }
        default:
          {
            System.out.println("Метод не разрешен – " + requestMethod);
            exchange.sendResponseHeaders(405, 0);
          }
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    } finally {
      exchange.close();
    }
  }

  private int parseIntPath(String path) {
    try {
      return Integer.parseInt(path);
    } catch (NumberFormatException exception) {
      return -1;
    }
  }

  public void startServer() {
    server.start();
    System.out.println("Started TaskServer " + PORT);
    System.out.println("http://localhost:" + PORT + "/tasks");
  }

  public void stopServer() {
    server.stop(0);
    System.out.println("Stopped server on PORT: " + PORT);
  }

  public String readText(HttpExchange exchange) throws IOException {
    return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
  }

  public void writeText(HttpExchange exchange, String text, int statusCode) throws IOException {
    Headers headers = exchange.getResponseHeaders();
    headers.set("Content-Type", "application/json;charset=utf-8");
    exchange.sendResponseHeaders(statusCode, 0);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(text.getBytes());
    }
  }
}
