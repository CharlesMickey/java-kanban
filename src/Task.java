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

  //    public void setStatus(Status status) {
  //        this.status = status;
  //    }

  @Override
  public String toString() {
    return (
      "\nTask{" +
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
