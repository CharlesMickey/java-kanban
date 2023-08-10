package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import model.Epic;
import model.Subtask;
import model.Task;
import server.KVTaskClient;
import taskFileManager.FileBackedTasksManager;

public class HttpTaskManager extends FileBackedTasksManager {

  private final KVTaskClient client;
  private final Gson gson;

  public HttpTaskManager(String url) {
    super(url);
    gson = Managers.getGson();
    this.client = new KVTaskClient(url);
    loadFromServer();
  }

  @Override
  protected void save() {
    client.put("task", gson.toJson(getAllTasks()));
    client.put("epic", gson.toJson(getAllEpics()));
    client.put("subtask", gson.toJson(getAllSubtasks()));
    client.put("history", gson.toJson(historyToString(getHistory())));
    client.put("prioritized", gson.toJson(getPrioritizedTasks()));
  }

  public void loadFromServer() {
    List<Task> tasks = gson.fromJson(client.load("task"), new TypeToken<List<Task>>() {}.getType());
    List<Epic> epics = gson.fromJson(client.load("epic"), new TypeToken<List<Epic>>() {}.getType());
    List<Subtask> subtasks = gson.fromJson(client.load("subtask"), new TypeToken<List<Subtask>>() {}.getType());

    if (tasks != null) {
      tasks.forEach(t -> setTask(t.getId(), t));
    }
    if (epics != null) {
      epics.forEach(t -> setEpic(t.getId(), t));
    }
    if (subtasks != null) {
      subtasks.forEach(t -> setSubtask(t.getId(), t));
    }
  }
}
