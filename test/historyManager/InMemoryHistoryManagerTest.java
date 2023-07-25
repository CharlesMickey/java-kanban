package historyManager;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import model.Status;
import model.Task;
import model.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {

  private InMemoryHistoryManager imht;

  private Task createTask() {
    return new Task(Type.TASK, "В магаз", "Купить 5 шоколадок", Status.NEW);
  }

  @BeforeEach
  void setUp() {
    imht = new InMemoryHistoryManager();
  }

  @Test
  void getHistory() {
    List<Task> historyListWithoutTask = imht.getHistory();

    assertTrue(historyListWithoutTask.isEmpty(), "Списиок истори не пустой");

    Task task = createTask();

    imht.add(task);

    List<Task> historyListWithTask = imht.getHistory();

    assertFalse(historyListWithTask.isEmpty(), "Списиок истори пустой");
  }

  @Test
  void remove() {
    Task task1 = createTask();
    Task task2 = createTask();
    Task task3 = createTask();
    Task task4 = createTask();
    Task task5 = createTask();

    imht.add(task1);
    imht.add(task2);
    imht.add(task3);
    imht.add(task4);
    imht.add(task5);

    List<Task> historyListWithTask = imht.getHistory();

    assertEquals(historyListWithTask.size(), 5, "Количество задач в истории не 5");
    assertTrue(historyListWithTask.contains(task1), "Задачи нет в спсике");

    imht.remove(task3.getId());

    List<Task> historyListAfterRemoveCenterTask = imht.getHistory();

    assertEquals(historyListAfterRemoveCenterTask.size(), 4, "Количество задач в истории не 4");
    assertFalse(historyListAfterRemoveCenterTask.contains(task3), "Задача оасталась в спсике");

    imht.remove(task1.getId());

    List<Task> historyListAfterRemoveStartTask = imht.getHistory();

    assertEquals(historyListAfterRemoveStartTask.size(), 3, "Количество задач в истории не 3");
    assertFalse(historyListAfterRemoveStartTask.contains(task1), "Задача оасталась в спсике");

    imht.remove(task5.getId());

    List<Task> historyListAfterRemoveLastTask = imht.getHistory();

    assertEquals(historyListAfterRemoveLastTask.size(), 2, "Количество задач в истории не 2");
    assertFalse(historyListAfterRemoveLastTask.contains(task5), "Задача оасталась в спсике");
  }

  @Test
  void add() {
    List<Task> historyListWithoutTask = imht.getHistory();

    assertTrue(historyListWithoutTask.isEmpty(), "Списиок истори не пустой");

    Task task1 = createTask();
    Task task2 = createTask();

    imht.add(task1);
    imht.add(task2);
    imht.add(task1);
    imht.add(task2);

    List<Task> historyListWithTask = imht.getHistory();

    assertEquals(historyListWithTask.size(), 2, "Количество задач в истории не 2");
  }
}
