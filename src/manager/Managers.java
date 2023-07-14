package manager;

import historyManager.HistoryManager;
import historyManager.InMemoryHistoryManager;
import taskFileManager.FileBackedTasksManager;
import taskManager.InMemoryTaskManager;
import taskManager.TaskManager;


public class Managers {

  public static TaskManager getDefault(String filePath) {
    return new FileBackedTasksManager(filePath);
  }

  public static HistoryManager getDefaultHistory() {
    return new InMemoryHistoryManager();
  }
}
