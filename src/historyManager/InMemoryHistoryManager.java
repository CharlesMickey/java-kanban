package historyManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Task;

public class InMemoryHistoryManager implements HistoryManager {

  private final CustomLinkedList taskList;

  private final Map<Integer, Node> taskNodesForHistory;

  public InMemoryHistoryManager() {
    this.taskList = new CustomLinkedList();
    this.taskNodesForHistory = new HashMap<>();
  }

  @Override
  public List<Task> getHistory() {
    return List.copyOf(taskList.getTasks());
  }

  @Override
  public void remove(int id) {
    Node curNode = taskNodesForHistory.get(id);
    if (curNode != null) {
      taskList.removeNode(curNode);
      taskNodesForHistory.remove(id);
    }
  }

  @Override
  public void add(Task task) {
    Node curNode = taskNodesForHistory.get(task.getId());
    if (curNode != null) {
      taskList.removeNode(curNode);
    }
    taskList.linkLast(task);
  }

  class CustomLinkedList {

    private Node head;
    private Node tail;
    private int size = 0;

    public void linkLast(Task task) {
      Node oldTail = tail;
      Node newTail = new Node(task);

      if (head == null) {
        head = newTail;
      } else if (oldTail == null) {
        head.next = newTail;
        newTail.prev = head;
        tail = newTail;
      } else {
        oldTail.next = newTail;
        newTail.prev = oldTail;
        tail = newTail;
      }
      taskNodesForHistory.put(task.getId(), newTail);
      size++;
    }

    public ArrayList<Task> getTasks() {
      ArrayList<Task> listTask = new ArrayList<>();
      Node curNode = head;

      while (curNode != null) {
        listTask.add(curNode.task);
        curNode = curNode.next;
      }
      return listTask;
    }

    void removeNode(Node node) {
      Node prevNode = node.prev;
      Node nextNode = node.next;

      if (prevNode != null) {
        prevNode.next = nextNode;
      } else {
        head = nextNode;
      }

      if (nextNode != null) {
        nextNode.prev = prevNode;
      } else {
        tail = prevNode;
      }
      size--;
    }
  }

  class Node {

    public Task task;
    public Node next;
    public Node prev;

    public Node(Task task) {
      this.task = task;
    }
  }
}
