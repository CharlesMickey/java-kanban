package taskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

  @Override
  protected InMemoryTaskManager createTaskManager() {
    return new InMemoryTaskManager();
  }
}
