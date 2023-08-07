package server;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Постман: https://www.getpostman.com/collections/a83b61d9e1c81c10575c
 */
public class KVServer {

  public static final int PORT = 8078;
  public static final String HOST_NAME = "localhost";
  private final String apiToken;
  private final HttpServer server;
  private final Map<String, String> data = new HashMap<>();

  public KVServer() throws IOException {
    apiToken = generateApiToken();
    server = HttpServer.create(new InetSocketAddress(HOST_NAME, PORT), 0);
    server.createContext("/register", this::register);
    server.createContext("/save", this::save);
    server.createContext("/load", this::load);
  }

  public String getServer() {
    return "http://" + HOST_NAME + ":" + PORT;
  }

  private void load(HttpExchange h) throws IOException {
    try {
      System.out.println("\n/load");
      if (!hasAuth(h)) {
        System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
        h.sendResponseHeaders(403, 0);
        return;
      }
      if ("GET".equals(h.getRequestMethod())) {
        String key = h.getRequestURI().getPath().substring("/load/".length());
        if (key.isEmpty()) {
          System.out.println("Key для отправки пустой. key указывается в пути: /load/{key}");
          h.sendResponseHeaders(400, 0);
          return;
        }
        String value = data.get(key);
        if (value.isEmpty()) {
          System.out.println("Value для отправки пустой. value указывается в теле запроса");
          h.sendResponseHeaders(400, 0);
          return;
        }

        sendText(h, value);
      } else {
        System.out.println("/load ждёт POST-запрос, а получил: " + h.getRequestMethod());
        h.sendResponseHeaders(405, 0);
      }
    } finally {
      h.close();
    }
  }

  private void save(HttpExchange h) throws IOException {
    try {
      System.out.println("\n/save");
      if (!hasAuth(h)) {
        System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
        h.sendResponseHeaders(403, 0);
        return;
      }
      if ("POST".equals(h.getRequestMethod())) {
        String key = h.getRequestURI().getPath().substring("/save/".length());
        if (key.isEmpty()) {
          System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
          h.sendResponseHeaders(400, 0);
          return;
        }
        String value = readText(h);
        if (value.isEmpty()) {
          System.out.println("Value для сохранения пустой. value указывается в теле запроса");
          h.sendResponseHeaders(400, 0);
          return;
        }
        data.put(key, value);
        System.out.println("Значение для ключа " + key + " успешно обновлено!");
        h.sendResponseHeaders(200, 0);
      } else {
        System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
        h.sendResponseHeaders(405, 0);
      }
    } finally {
      h.close();
    }
  }

  private void register(HttpExchange h) throws IOException {
    try {
      System.out.println("\n/register");
      if ("GET".equals(h.getRequestMethod())) {
        sendText(h, apiToken);
      } else {
        System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
        h.sendResponseHeaders(405, 0);
      }
    } finally {
      h.close();
    }
  }

  public void start() {
    System.out.println("Запускаем сервер на порту " + PORT);
    System.out.println("Открой в браузере http://localhost:" + PORT + "/");
    System.out.println("API_TOKEN: " + apiToken);
    server.start();
  }

  public void stop() {
    System.out.println("Sever stopped");
    server.stop(0);
  }

  private String generateApiToken() {
    return "" + System.currentTimeMillis();
  }

  protected boolean hasAuth(HttpExchange h) {
    String rawQuery = h.getRequestURI().getRawQuery();
    return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
  }

  protected String readText(HttpExchange h) throws IOException {
    return new String(h.getRequestBody().readAllBytes(), UTF_8);
  }

  protected void sendText(HttpExchange h, String text) throws IOException {
    byte[] resp = text.getBytes(UTF_8);
    h.getResponseHeaders().add("Content-Type", "application/json");
    h.sendResponseHeaders(200, resp.length);
    h.getResponseBody().write(resp);
  }
}
