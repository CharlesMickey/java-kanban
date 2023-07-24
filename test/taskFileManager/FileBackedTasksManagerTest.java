package taskFileManager;

import org.junit.jupiter.api.Test;
import taskManager.TaskManagerTest;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @Override
    protected  FileBackedTasksManager createTaskManager() {
        return new FileBackedTasksManager("./test/testResources/test_data.csv");
    }


    @Test
    void loadFromFile() {
    }
}