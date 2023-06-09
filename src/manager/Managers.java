package manager;

import historyManager.HistoryManager;
import historyManager.InMemoryHistoryManager;
import taskManager.InMemoryTaskManager;
import taskManager.TaskManager;

// Добавил final чтобы случайно не создать подклассы этого класса или изменить его поведение
public class Managers {

  public static TaskManager getDefault() {
    return new InMemoryTaskManager();
  }

  public static HistoryManager getDefaultHistory() {
    return new InMemoryHistoryManager();
  }
}