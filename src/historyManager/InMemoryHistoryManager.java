package historyManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Task;

public class InMemoryHistoryManager implements HistoryManager {

  private final Map<Integer, Task> taskForHistory;

  private final int MAX_ARRAY_ITEMS = 10;

  public InMemoryHistoryManager() {
    this.taskForHistory = new HashMap<>();
  }

  @Override
  public List<Task> getHistory() {
    return List.copyOf(taskForHistory.values());
  }

  @Override
  public  void  remove(int id) {
    taskForHistory.remove(id);
  }

  @Override
  public void add(Task task) {
    if (taskForHistory.size() == MAX_ARRAY_ITEMS) {
      taskForHistory.remove(0);
      taskForHistory.put(task.getId(), task);
    } else {
      taskForHistory.put(task.getId(), task);
    }
  }

}
