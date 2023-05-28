package model;

import java.util.UUID;

public class Task {

  private String id;
  private String name;
  private String description;
  private Status status;

  public Task(String name, String description, Status status, String id) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.status = status;
  }

  /*UUID использую для приближения к реальному id. 
  Для отслеживания порядка и т.д. в дальнейшем (на мой взгляд)
   лучше добавить поле с датой-временем создания таски*/

  public Task(String name, String description, Status status) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.description = description;
    this.status = status;
  }

  public Task(String name, String description) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
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
