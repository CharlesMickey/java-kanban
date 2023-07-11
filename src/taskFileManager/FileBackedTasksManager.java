package taskFileManager;

import model.Epic;
import model.Subtask;
import model.Task;
import taskManager.InMemoryTaskManager;
import taskManager.TaskManager;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    public void save() {

    }
    @Override
    public void setTask(Integer id, Task task) {
        super.setTask(id, task);
        save();
    }

    @Override
    public void setEpic(Integer id, Epic epic) {
        super.setEpic(id, epic);
        save();
    }

    @Override
    public void setSubtask(Integer id, Subtask subtask) {
        super.setSubtask(id, subtask);
        save();
    }

    @Override
    public void deleteTaskOfAnyTypeById(Integer id) {
        super.deleteTaskOfAnyTypeById(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void updateAnyTypeOfTask(Integer id, Task updatedTask) {
        super.updateAnyTypeOfTask(id, updatedTask);
        save();
    }
}
