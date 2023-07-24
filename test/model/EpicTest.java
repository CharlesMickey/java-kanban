package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskManager.InMemoryTaskManager;

class EpicTest {

  private InMemoryTaskManager inMemoryTaskManager;
  private Epic newEpic;

  private Subtask createNewSubtask(Status status) {
    return new Subtask(Type.SUBTASK, "Купить билеты", "Купить билеты на поезд в Анапу", status, newEpic.getId());
  }

  @BeforeEach
  void addInitialData() {
    inMemoryTaskManager = new InMemoryTaskManager();
    newEpic = new Epic(Type.EPIC, "EpicTestName", "EpicTestDescription");
  }

  @Test
  void shouldReturnNEWStatusWithoutSubtask() {
    Status epicStatus = newEpic.getStatus();
    assertEquals(epicStatus, Status.NEW);
  }

  @Test
  void shouldReturnNEWStatusWhenAllSubtasksWithNEWStatus() {
    Subtask subtaskNEW1 = createNewSubtask(Status.NEW);
    Subtask subtaskNEW2 = createNewSubtask(Status.NEW);

    inMemoryTaskManager.setEpic(newEpic.getId(), newEpic);
    inMemoryTaskManager.setSubtask(subtaskNEW1.getId(), subtaskNEW1);
    inMemoryTaskManager.setSubtask(subtaskNEW2.getId(), subtaskNEW2);

    Status epicStatus = newEpic.getStatus();

    assertEquals(epicStatus, Status.NEW);
  }

  @Test
  void shouldReturnDONEStatusWhenAllSubtasksWithDONEStatus() {
    Subtask subtaskNEW1 = createNewSubtask(Status.DONE);
    Subtask subtaskNEW2 = createNewSubtask(Status.DONE);

    inMemoryTaskManager.setEpic(newEpic.getId(), newEpic);
    inMemoryTaskManager.setSubtask(subtaskNEW1.getId(), subtaskNEW1);
    inMemoryTaskManager.setSubtask(subtaskNEW2.getId(), subtaskNEW2);

    Status epicStatus = newEpic.getStatus();

    assertEquals(epicStatus, Status.DONE);
  }

  @Test
  void shouldReturnIN_PROGRESSStatusWhenSubtasksWithNEWAndDONEStatus() {
    Subtask subtaskNEW1 = createNewSubtask(Status.NEW);
    Subtask subtaskNEW2 = createNewSubtask(Status.DONE);

    inMemoryTaskManager.setEpic(newEpic.getId(), newEpic);
    inMemoryTaskManager.setSubtask(subtaskNEW1.getId(), subtaskNEW1);
    inMemoryTaskManager.setSubtask(subtaskNEW2.getId(), subtaskNEW2);

    Status epicStatus = newEpic.getStatus();

    assertEquals(epicStatus, Status.IN_PROGRESS);
  }

  @Test
  void shouldReturnIN_PROGRESSStatusWhenAllSubtasksWithIN_PROGRESSStatus() {
    Subtask subtaskNEW1 = createNewSubtask(Status.IN_PROGRESS);
    Subtask subtaskNEW2 = createNewSubtask(Status.IN_PROGRESS);

    inMemoryTaskManager.setEpic(newEpic.getId(), newEpic);
    inMemoryTaskManager.setSubtask(subtaskNEW1.getId(), subtaskNEW1);
    inMemoryTaskManager.setSubtask(subtaskNEW2.getId(), subtaskNEW2);

    Status epicStatus = newEpic.getStatus();

    assertEquals(epicStatus, Status.IN_PROGRESS);
  }
}
