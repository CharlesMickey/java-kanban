package model;

public class Task {

  private static int idCount = 0;
  private int id;
  private String name;
  private String description;
  private Status status;

  public Task(String name, String description, Status status, int id) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.status = status;
  }

  public Task(String name, String description, Status status) {
    this.id = idCount + 1;
    idCount++;
    this.name = name;
    this.description = description;
    this.status = status;
  }

  public Task(String name, String description) {
    this.id = idCount + 1;
    idCount++;
    this.name = name;
    this.description = description;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Status getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return (
      "\nmodel.Task{" +
      "id=" +
      id +
      ", name='" +
      name +
      '\'' +
      ", description='" +
      description +
      '\'' +
      ", status=" +
      status +
      '}'
    );
  }
}
