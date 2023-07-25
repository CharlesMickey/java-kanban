package taskFileManager;

import static org.junit.jupiter.api.Assertions.*;

import model.*;
import org.junit.jupiter.api.Test;
import taskManager.TaskManagerTest;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

  private static final String PATH = "./test/testResources/test_data.csv";

  @Override
  protected FileBackedTasksManager createTaskManager() {
    return new FileBackedTasksManager(PATH);
  }

  @Test
  void loadFromFile() {
    assertTrue(taskManager.getAllSubtasks().isEmpty(), "Список подзадач не пустой");
    taskManager.save();
    FileBackedTasksManager fbtm = taskManager.loadFromFile(PATH);

    assertTrue(fbtm.getAllSubtasks().isEmpty(), "Список подзадач не пустой");

    Epic epic = new Epic(Type.EPIC, "Подготовка к путешествию", "Подготовиться к поездке на озеро");
    Subtask subtask = new Subtask(
      Type.SUBTASK,
      "Купить билеты",
      "Купить билеты на поезд в Анапу",
      Status.NEW,
      epic.getId()
    );
    Task task = new Task(Type.TASK, "В магаз", "Купить 5 шоколадок", Status.NEW);
    taskManager.setEpic(epic.getId(), epic);
    taskManager.setTask(task.getId(), task);

    FileBackedTasksManager fbtmWithTasks = taskManager.loadFromFile(PATH);
    Epic epic2 = fbtmWithTasks.getEpic(epic.getId());

    assertTrue(fbtmWithTasks.getAllSubtasks().isEmpty(), "Список подзадач не пустой");
    assertNull(epic2.getSubtasks(), "Список подзадач не пустой");
    taskManager.setSubtask(subtask.getId(), subtask);

    FileBackedTasksManager fbtmWithSubtasks = taskManager.loadFromFile(PATH);
    Epic epic3 = fbtmWithSubtasks.getEpic(epic.getId());

    assertFalse(fbtmWithSubtasks.getAllSubtasks().isEmpty(), "Список подзадач пустой");
    assertNotNull(epic3.getSubtasks(), "Список подзадач пустой");
  }
}
