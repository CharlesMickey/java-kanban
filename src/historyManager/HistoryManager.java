package historyManager;

import java.util.List;
import model.Task;

public interface HistoryManager {
  void add(Task task);

  List<Task> getHistory();
}
