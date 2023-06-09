package historyManager;

import java.util.ArrayList;
import java.util.List;
import model.Task;

public class InMemoryHistoryManager implements HistoryManager {

  private final List<Task> taskForHistory;

  private final int MAX_ARRAY_ITEMS = 10;

  public InMemoryHistoryManager() {
    this.taskForHistory = new ArrayList<>();
  }

  @Override
  public List<Task> getHistory() {
    return new ArrayList<>(taskForHistory);
  }

  @Override
  public void add(Task task) {
    if (taskForHistory.size() == MAX_ARRAY_ITEMS) {
      taskForHistory.remove(0);
      taskForHistory.add(task);
    } else {
      taskForHistory.add(task);
    }
  }
}
