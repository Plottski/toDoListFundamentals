package plottski.todolistfundamentals.Entities;
import jakarta.persistence.*;

import java.io.Serializable;


@jakarta.persistence.Entity
public class UserForDB implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column
    private boolean isLoggedIn;

    @ManyToOne
    private UserItemList collaborators;

    public UserForDB() {

    }

    public UserForDB(String username, String password, String email, boolean isLoggedIn) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLoggedIn = isLoggedIn;
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

    public UserItemList getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(UserItemList collaborators) {
        this.collaborators = collaborators;
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
