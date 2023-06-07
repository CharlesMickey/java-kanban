package manager;

import historyManager.HistoryManager;
import historyManager.InMemoryHistoryManager;

public final class Managers {

  public static TaskManager getDefault() {
    return new InMemoryTaskManager();
  }

  public static HistoryManager getDefaultHistory() {
    return new InMemoryHistoryManager();
  }
}
