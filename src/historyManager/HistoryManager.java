package historyManager;

import java.util.List;
import model.Task;

public interface HistoryManager {
  public void add(Task task);

  public List<Task> getHistory();
}
