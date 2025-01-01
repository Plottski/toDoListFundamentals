package plottski.todolistfundamentals.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;


@Entity
public class Item implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    private String listName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long creationTime;

    @Column(nullable = false)
    private String dueDate;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "todo_list_fk")
    ToDoList toDoList = new ToDoList();

    public Item() {

    }

    public Item(String title, String description, Long creationTime, User userID, String username, int listID, String listName, String dueDate, ToDoList toDoList) {
        this.title = title;
        this.description = description;
        this.creationTime = creationTime;
        this.dueDate = dueDate;
        this.toDoList = toDoList;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public ToDoList getTodoList() {
        return toDoList;
    }

    public void setTodoList(ToDoList toDoList) {
        this.toDoList = toDoList;
    }
}

