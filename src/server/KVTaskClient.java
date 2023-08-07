package server;

import static server.HttpTaskServer.DEFAULT_CHARSET;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

  private String token;
  private String url;

  public KVTaskClient(String url) {
    this.url = url;
    register(url);
    System.out.println("API " + token);
  }

  public void put(String key, String json) {
    URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + token);
    HttpRequest request = HttpRequest
      .newBuilder()
      .POST(HttpRequest.BodyPublishers.ofString(json, DEFAULT_CHARSET))
      .uri(uri)
      .version(HttpClient.Version.HTTP_1_1)
      .header("Accept", "text/html")
      .build();

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    try {
      HttpResponse<String> response = client.send(request, handler);

      int status = response.statusCode();

      if (status >= 200 && status <= 299) {
        System.out.println("Сервер успешно обработал запрос. Код состояния: " + status);
      } else {
        System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + status);
      }
    } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
      System.out.println(
        "Во время выполнения запроса ресурса по url-адресу: '" +
        uri +
        "' возникла ошибка.\n" +
        "Проверьте, пожалуйста, адрес и повторите попытку."
      );
    }
  }

  public String load(String key) {
    URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + token);
    HttpRequest request = HttpRequest
      .newBuilder()
      .GET()
      .uri(uri)
      .version(HttpClient.Version.HTTP_1_1)
      .header("Accept", "text/html")
      .build();

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    try {
      HttpResponse<String> response = client.send(request, handler);
      return response.body();
    } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
      System.out.println(
        "Во время выполнения запроса ресурса по url-адресу: '" +
        uri +
        "' возникла ошибка.\n" +
        "Проверьте, пожалуйста, адрес и повторите попытку."
      );
      return null;
    }
  }

  void register(String url) {
    URI uri = URI.create(url + "/register");
    HttpRequest request = HttpRequest
      .newBuilder()
      .GET()
      .uri(uri)
      .version(HttpClient.Version.HTTP_1_1)
      .header("Accept", "text/html")
      .build();

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    try {
      HttpResponse<String> response = client.send(request, handler);
      this.token = response.body();
    } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
      System.out.println(
        "Во время выполнения запроса ресурса по url-адресу: '" +
        uri +
        "' возникла ошибка.\n" +
        "Проверьте, пожалуйста, адрес и повторите попытку."
      );
    }
  }
}
