import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {

    private HashMap<String, Task> tasks;
    private HashMap<String, Epic> epics;
    private HashMap<String, Subtask> subtasks;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public HashMap<String, Task> getTasks() {
        return tasks;
    }

    public void setTasks(String id, Task task) {
        tasks.put(id, task);
    }

    public HashMap<String, Epic> getEpics() {
        return epics;
    }

    public void setEpics(String id, Epic epic) {
        epics.put(id, epic);
    }

    public HashMap<String, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(String id, Subtask subtask) {
        subtasks.put(id, subtask);
    }

    public Task getTaskById(String id) {
        Task task = tasks.get(id);
        if (task == null) {
            task = epics.get(id);
        }
        if (task == null) {
            task = subtasks.get(id);
        }
        return task;
    }

    public List<Subtask> getSubtasksById(String id) {
        return epics.get(id).getSubtasks();
    }

    public void deleteTaskById(String id) {
        Task task = getTaskById(id);
        if (task instanceof Epic) {
            epics.remove(id);
        } else if (task instanceof Subtask) {
            subtasks.remove(id);
            Epic epic = epics.get(((Subtask) task).getEpicId());
            if (epic != null) {
                epic.deleteSubtask((Subtask) task);
            }
        } else {
            tasks.remove(id);
        }
    }
}
