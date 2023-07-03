package manager;

import historyManager.HistoryManager;
import historyManager.InMemoryHistoryManager;
import taskManager.InMemoryTaskManager;
import taskManager.TaskManager;

public class Managers {

  public static TaskManager getDefault() {
    return new InMemoryTaskManager();
  }

  public static HistoryManager getDefaultHistory() {
    return new InMemoryHistoryManager();
  }
}
