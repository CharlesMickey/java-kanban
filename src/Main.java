import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task = new Task("В магаз", "Купить 5 шоколадок", Status.NEW);
        Task task2 = new Task("В Макдак", "Купить пару флурри и роял", Status.IN_PROGRESS);

        Subtask subtask1 = new Subtask(
                "Купить билеты",
                "Купить билеты на поезд в Анапу",
                Status.IN_PROGRESS
        );
        Subtask subtask2 = new Subtask(
                "Забронировать отель",
                "Забронировать номер в отеле в СПб",
                Status.NEW
        );
        Subtask subtask3 = new Subtask(
                "Купить краску",
                "Купить краску – фиолетовую",
                Status.NEW
        );

        Epic epic1 = new Epic(
                "Подготовка к путешествию",
                "Подготовиться к поездке на озеро",
                new ArrayList<>(List.of(subtask1, subtask2))
        );
        Epic epic2 = new Epic(
                "Ремонт в квартире",
                "Сделать ремонт в падике",
                new ArrayList<>(List.of(subtask3))
        );

        taskManager.setTasks(task.getId(), task);
        taskManager.setTasks(task2.getId(), task2);

        taskManager.setEpics(epic1.getId(), epic1);
        taskManager.setEpics(epic2.getId(), epic2);

        taskManager.setSubtasks(subtask1.getId(), subtask1);
        taskManager.setSubtasks(subtask2.getId(), subtask2);
        taskManager.setSubtasks(subtask3.getId(), subtask3);

        System.out.println("\nДобавили");
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());


        System.out.println("\nОбновили");

        Subtask subtask3Upd = new Subtask(
                "Остаемся в Ленинграде",
                "Идем в Бикмагентс",
                Status.DONE,
                subtask3.getId(),
                subtask3.getEpicId()
        );

        Task taskUpd = new Task(
                "Лучше в погреб",
                "За свеклой",
                Status.DONE,
                task.getId()
        );


        taskManager.updateAnyTypeOfTask(subtask3.getId(), subtask3Upd);
        taskManager.updateAnyTypeOfTask(task.getId(), taskUpd);




        System.out.println("\nВесь список задач");


        System.out.println(taskManager.getAllEpics());


        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubtasks());


        taskManager.deleteTaskById(epic1.getId());
        taskManager.deleteTaskById(task2.getId());
        taskManager.deleteTaskById(subtask3.getId());


        System.out.println("Удалили");

        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
    }
}
