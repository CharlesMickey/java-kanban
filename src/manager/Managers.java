package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import historyManager.HistoryManager;
import historyManager.InMemoryHistoryManager;
import java.time.LocalDateTime;
import taskFileManager.FileBackedTasksManager;
import taskManager.TaskManager;
import utils.LocalDateAdapter;

public class Managers {

  public static TaskManager getDefault(String url) {
    return new HttpTaskManager(url);
  }

  public static HistoryManager getDefaultHistory() {
    return new InMemoryHistoryManager();
  }

  public static Gson getGson() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
    gsonBuilder.serializeNulls();
    return gsonBuilder.create();
  }
}
