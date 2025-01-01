package plottski.todolistfundamentals.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class ToDoList implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String listName;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "user_fk")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "toDoList")
    private List<Item> items;

    public ToDoList() {

    }

    public ToDoList(String listName, String username, User user, List<Item> items, List<User> collaborators) {
        this.listName = listName;
        this.user = user;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public User getUser() {
        return user;
    }

    public void setUserForDB(User user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}