package plottski.todolistfundamentals.Entities;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.List;


@jakarta.persistence.Entity
@Table(name = "users")
public class User implements Serializable{

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

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    //Apparently with having multiple json annotations you need to define pathways otherwise you will get a deserialization error.
    // This is kind of like direct wiring the serialization traffic from what I read on the annotation and the fix since the values have to match.
    // This is what I understood
    @JsonManagedReference(value = "user-lists")
    private List<ItemList> itemLists;

    @ManyToMany
    private List<ItemList> collaborations;

    public User() {
    }

    public User(String username, String password, String email, boolean isLoggedIn, List<ItemList> itemLists, List<ItemList> collaborations) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLoggedIn = isLoggedIn;
        this.itemLists = itemLists;
        this.collaborations = collaborations;
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

    public List<ItemList> getItemLists() {
        return itemLists;
    }

    public void setItemLists(List<ItemList> itemLists) {
        this.itemLists = itemLists;
    }

    public List<ItemList> getCollaborations() {
        return collaborations;
    }

    public void setCollaborations(List<ItemList> collaborations) {
        this.collaborations = collaborations;
    }
}
