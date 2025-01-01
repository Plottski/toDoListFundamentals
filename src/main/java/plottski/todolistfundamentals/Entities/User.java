package plottski.todolistfundamentals.Entities;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;


@jakarta.persistence.Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column
    private boolean isLoggedIn;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ToDoList> toDoLists;

    public User() {

    }

    public User(int id, String username, String password, String email, boolean isLoggedIn, List<ToDoList> toDoLists) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLoggedIn = isLoggedIn;
        this.toDoLists = toDoLists;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public List<ToDoList> getTodoLists() {
        return toDoLists;
    }

    public void setUserItemListList(List<ToDoList> toDoListList) {
        this.toDoLists = toDoListList;
    }

    /*

    public UserForDB(String username, String password, String email, boolean isLoggedIn) {
        //this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLoggedIn = isLoggedIn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    } */
}
