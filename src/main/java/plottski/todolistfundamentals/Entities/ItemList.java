package plottski.todolistfundamentals.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class ItemList {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String listName;

    @OneToMany(mappedBy = "itemList", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "item-list")
    private List<Item> items;

    @ManyToOne
    @JsonBackReference(value = "user-lists")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "collaboration",
            joinColumns = @JoinColumn(name = "item_list_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> collaborators;


    public ItemList() {
    }

    public ItemList(String listName, List<Item> items, User user, List<User> collaborators) {
        this.listName = listName;
        this.items = items;
        this.user = user;
        this.collaborators = collaborators;
    }

    public int getId() {
        return id;
    }

    public List<User> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<User> collaborators) {
        this.collaborators = collaborators;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
