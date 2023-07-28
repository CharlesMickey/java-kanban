package manager;

import historyManager.HistoryManager;
import historyManager.InMemoryHistoryManager;
import taskFileManager.FileBackedTasksManager;
import taskManager.TaskManager;

public class Managers {

  private static final String PATH = "./resources/data.csv";

  public static TaskManager getDefault() {
    return new FileBackedTasksManager(PATH);
  }

  public static HistoryManager getDefaultHistory() {
    return new InMemoryHistoryManager();
  }
}
